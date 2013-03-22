--- Enthält klassenspezifische Ladefunktionen für die Klasse ShipFrame
local args = {...}

local configObject = args[1]

local configMethods = {}

function configMethods.socket(_id)
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

		if _data.location then
			if type(_data.location) ~= "table" then
				error("location must be a vector", 2)
			end
			loadObject.transform:setLocation(_data.location)
		elseif _data.x or _data.y or _data.z then
			loadObject:setLocation(_data.x or 0, _data.y or 0, _data.z or 0)
		end


		if _data.xRot or _data.yRot or _data.zRot then
			loadObject:setRotation(_data.xRot or 0, _data.yRot or 0, _data.zRot or 0)
		end

		configObject:addSocket(_id, loadObject)
	end
end

function configMethods.bindsocket(_id, _module)
	configObject:prebindModule(_id, _module)
end


local events = {}

function events.onUpdate(handler)

end

return configMethods, events