


-- replace loadfile and dofile

loadfile = function( _sFile, mode, env )
	local success, data = loadTextFile(_sFile)
	if success then
		local func, err = load( data, _sFile, mode or "bt", env or _ENV )
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


-- various additions to default libs

function string.starts(String,Start)
   return string.sub(String,1,string.len(Start))==Start
end

function string.ends(String,End)
   return End=='' or string.sub(String,-string.len(End))==End
end


-- load libraries

dofile("Scripts/vector.lua")
dofile("Scripts/quaternion.lua")