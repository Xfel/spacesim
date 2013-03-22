local args = {...}

local configObject = args[1]

local configMethods = {}

local events = {}

-- accepts the source and the drive force (number). 
-- returns the energy cost (number)
function events.computeEnergyCost(handler)
	
	configObject.energyCostComputer = handler;
	
end

return configMethods, events