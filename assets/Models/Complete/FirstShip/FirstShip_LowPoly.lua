
class "ShipFrame"

id="FirstShip"
name="First simple ship"

modelName="Models/Complete/FirstShip/FirstShip_LowPoly.blend"

mass=20
integrity=2000

socket "maindrive.left" {
	types = {"engine"},
	x = -3.6569, y = 0, z = -2.2305,
}

--bindsocket("maindrive.left", "Models/Complete/SimpleMainDrive/SimpleMainDrive.lua")