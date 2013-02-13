


vector3 = { x = 0, y = 0, z = 0 }

function vector3:add(other)
	return vector3:new(
	self.x + other.x,
	self.y + other.y,
	self.z + other.z
	)
end

function vector3:sub(other)
	return vector3:new(
	self.x - other.x,
	self.y - other.y,
	self.z - other.z
	)
end

function vector3:mul(fact)
	return vector3.new(
	self.x * fact,
	self.y * fact,
	self.z * fact
	)
end

function vector3:cross(other)
	return vector3:new{
		x = ((self.y * other.z) - (self.z * other.y)),
		x = ((self.z * other.x) - (self.x * other.z)),
		z = ((self.x * other.y) - (self.y * other.x))
	}
end

function vector3:dot(other)
	return self.x*other.x + self.y*other.y * self.z*other.z
end

function vector3:len()
	return math.sqrt(vector3.dot(self,self))
end

function vector3:normalize()
	return self:mul(1/self:len());
end


vector3.__index = vector3

vector3.__add = vector3.add
vector3.__sub = vector3.sub
vector3.__mul = vector3.mul
vector3.__unm = function(vec)
	return vec:mul(-1)
end

vector3.__metatable="vector3"

vector3.__tostring = function(vec)
	return "("..vec.x..","..vec.y..","..vec.z..")"
end

vector3.__eq = function (vec1, vec2)
	return vec1.x==vec2.x and vec1.y==vec2.y and vec1.z==vec2.z
end

function vector3:new(o)
	o = o or {}

	return setmetatable(o, self)
end

