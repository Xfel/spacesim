


vector3 = { x = 0, y = 0, z = 0 }

function vector3.add(vec1, vec2)
	return vector3.new(
	vec1.x + vec2.x,
	vec1.y + vec2.y,
	vec1.z + vec2.z
	)
end

function vector3.sub(vec1, vec2)
	return vector3.new(
	vec1.x - vec2.x,
	vec1.y - vec2.y,
	vec1.z - vec2.z
	)
end

function vector3.mul(vec1, fact)
	return vector3.new(
	vec1.x * fact,
	vec1.y * fact,
	vec1.z * fact
	)
end


local vector3_mt = {
	__index = vector3,

	__add = vector3.add,
	__sub = vector3.sub,
	__mul = vector3.mul,
	__unm = function(vec)
		return vec:mul(-1)
	end,
	
	__metatable="vector3"
}

function vector3.new(x, y, z)
	local o = { x=x, y=y, z=z }

	return setmetatable(o, vector3_mt)
end

function vector3.new(o)
	o = o or {}

	return setmetatable(o, vector3_mt)
end

