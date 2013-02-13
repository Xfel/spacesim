


quaternion = { x = 0, y = 0, z = 0, w = 1 }

function quaternion:new(o)
	o = o or {}

	return setmetatable(o, self)
end

function quaternion:fromAxisAngle(axis, angle)
	local len = math.sqrt(vector3.dot(axis, axis))
	local sin = math.sin(angle/2)/len
	return quaternion:new{
		x=axis.x*sin,
		y=axis.y*sin,
		z=axis.z*sin,
		w=math.cos(angle/2)
	}
end

function quaternion:mul(other)
	if type(other) == "number" then

	elseif other.x and other.y and other.z then

		if other.w then

		else
			return vector3:new{
				x = self.w * self.w * other.x + 2 * self.y * self.w * other.z - 2 * self.z * self.w * other.y + self.x * self.x * other.x
				+ 2 * self.y * self.x * other.y + 2 * self.z * self.x * other.z - self.z * self.z * other.x - self.y * self.y * other.x,
				y = 2 * self.x * self.y * other.x + self.y * self.y * other.y + 2 * self.z * self.y * other.z + 2 * self.w * self.z
				* other.x - self.z * self.z * other.y + self.w * self.w * other.y - 2 * self.x * self.w * other.z - self.x * self.x
				* other.y,
				z = 2 * self.x * self.z * other.x + 2 * self.y * self.z * other.y + self.z * self.z * other.z - 2 * self.w * self.y * other.x
				- self.y * self.y * other.z + 2 * self.w * self.x * other.y - self.x * self.x * other.z + self.w * self.w * other.z}
		end
	end
end
