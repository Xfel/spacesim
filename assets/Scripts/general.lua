


-- replace loadfile and dofile

loadfile = function( _sFile, mode, env )
	local success, data = loadTextFile(_sFile)
	if success then
		local func, err = load( data, _sFile, mode, env )
		return func, err
	end
	return nil, data
end

dofile = function( _sFile )
	local fnFile, e = loadfile( _sFile )
	if fnFile then
		--setfenv( fnFile, getfenv(2) )
		return fnFile()
	else
		error( e, 2 )
	end
end

-- import function

local hiddenGlobals= {
	load=true,
	loadfile=true,
	dofile=true
}

local protectedEnvMT ={
	__metatable="The environment metatable is protected",


}
protectedEnvMT.__index = function(_table, _key)
	if hiddenGlobals[_key] then
		return nil
	end
	
	if _key == "_G" then
		return protectedEnvMT;
	end

	return _ENV[_key]
end

function loadConfig(code,name)
	local config={}

	setmetatable(config, protectedEnvMT)

	local cfg = load(code, name, "bt", config)
	cfg()
	return config
end

function import(name)
	local config={}

	setmetatable(config, protectedEnvMT)

	local cfg = loadfile(name, "bt", config)
	cfg()
	return config
end