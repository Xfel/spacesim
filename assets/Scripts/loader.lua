

-- config library

function config.import(name)
	local config={}

	setmetatable(config, protectedEnvMT)

	local cfg = loadfile(name, "bt", config)
	cfg()
	return config
end

-- init clas specific config methods

local function getConfigMethods(configObject, _clsid)

	if string.ends(_clsid, "ShipFrame") then
		return {
			socket=function(_id)
				if configObject:getSocket(_id) then
					error("Socket id duplication: " .. _id, 2)
				end

				return function(_data)
					local loadObject = config.newInstance("spacegame.model.structure.ModuleSocket")

					if _data.tiers then
						loadObject:setAllowedTiers(_data.tiers)
					end

					if _data.types then
						loadObject:setAllowedTypes(_data.types)
					end

					if _data.x or _data.y or _data.z then
						loadObject:setLocation(_data.x or 0, _data.y or 0, _data.z or 0)
					end


					if _data.xRot or _data.yRot or _data.zRot then
						loadObject:setRotation(_data.xRot or 0, _data.yRot or 0, _data.zRot or 0)
					end

					configObject:addSocket(_id, loadObject)
				end
			end,
			bindsocket = function(_id, _module)
				configObject:prebindModule(_id, _module)
			end
		}
	end

end

-- configure hidden global methods
local hiddenGlobals= {
	load=true,
	loadfile=true,
	dofile=true,
	config=true,
	loadConfig=true
}

-- load a lua config file...
function loadConfig(code, name)
	local configObject, configMethods

	local function class(_clsid)
		configObject = config.newInstance(_clsid);

		--configObject.location = name
		configMethods = getConfigMethods(configObject, _clsid)
	end

	local configEnv={}

	-- create protection metatable
	local configEnvMetatable = {}

	configEnvMetatable.__metatable = "Environment metatable is protected"

	configEnvMetatable.__index = function(_tbl, _key)
		if not configObject and _key == "class" then
			return class
		elseif configMethods and configMethods[_key] then
			return configMethods[_key]
		end

		if _key == "_G" then
			return configEnv
		end

		if hiddenGlobals[_key] then
			return nil
		end

		return config[_key] or _ENV[_key]
	end

	configEnvMetatable.__newindex = function(_tbl, _key, _value)
		if not configObject then
			error("No class specified", 2)
		end

		if type(_value) == "function" then
		-- TODO handle event
		else
			configObject[_key] = _value
		end
	end

	setmetatable(configEnv, configEnvMetatable)

	-- load and execute the file
	local func, err = load(code, name, "bt", configEnv)

	if not func then
		error(err)
	end

	func()

	-- reduce metatable to exclude loader functions
	configEnvMetatable.__newindex=nil
	configEnvMetatable.__index = function(_tbl, _key)
		if _key == "_G" then
			return configEnv
		end

		if hiddenGlobals[_key] then
			return nil
		end

		return configObject[_key] or _ENV[_key]
	end

	return configObject
end


