package net.whatamidoingstudios.lacroix.block.utils;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

//doesn't work. keeping here in case I ever want to fix

//axis 0 is x
//axis 1 is y
//axis 2 is z
public class AxisAlignedBBUtils {
	public static AxisAlignedBB rotateAABB(AxisAlignedBB aabb, Vec3d rotationpoint, int degreesX, int degreesY, int degreesZ) {
		double maxX = aabb.maxX;
		double maxY = aabb.maxY;
		double maxZ = aabb.maxZ;
		double minX = aabb.minX;
		double minY = aabb.minY;
		double minZ = aabb.minZ;
		
		double maxXp = maxX-rotationpoint.x;
		double maxYp = maxY-rotationpoint.y;
		double maxZp = maxZ-rotationpoint.z;
		
		double minXp = minX-rotationpoint.x;
		double minYp = minY-rotationpoint.y;
		double minZp = minZ-rotationpoint.z;
		
		double radiansX = degreesX*180/Math.PI;
		double radiansY = degreesY*180/Math.PI;
		double radiansZ = degreesZ*180/Math.PI;
		
		AxisAlignedBB rotatedX = rotateX(radiansX, new AxisAlignedBB(minXp, minYp, minZp, maxXp, maxYp, maxZp));
		AxisAlignedBB rotatedXY = rotateY(radiansY, rotatedX);
		AxisAlignedBB rotatedXYZ = rotateZ(radiansZ, rotatedXY);
		return new AxisAlignedBB(rotatedXYZ.minX+rotationpoint.x, rotatedXYZ.minY+rotationpoint.y, rotatedXYZ.minZ+rotationpoint.z, rotatedXYZ.maxX+rotationpoint.x, rotatedXYZ.maxY+rotationpoint.y, rotatedXYZ.maxZ+rotationpoint.z);
		
	}
	
	private static AxisAlignedBB rotateX(double radians, AxisAlignedBB aabb) {
		Math.cos(radians);
		double minY = aabb.minY*Math.cos(radians) - aabb.minZ*Math.sin(radians);
		double minZ = aabb.minZ*Math.cos(radians) - aabb.minY*Math.sin(radians);
		double maxY = aabb.maxY*Math.cos(radians) - aabb.maxZ*Math.sin(radians);
		double maxZ = aabb.maxZ*Math.cos(radians) - aabb.maxY*Math.sin(radians);
				
		return new AxisAlignedBB(aabb.minX, minY, minZ, aabb.maxX, maxY, maxZ);
	}
	
	private static AxisAlignedBB rotateY(double radians, AxisAlignedBB aabb) {
		Math.cos(radians);
		double minX = aabb.minX*Math.cos(radians) - aabb.minZ*Math.sin(radians);
		double minZ = aabb.minZ*Math.cos(radians) - aabb.minX*Math.sin(radians);
		double maxX = aabb.maxX*Math.cos(radians) - aabb.maxZ*Math.sin(radians);
		double maxZ = aabb.maxZ*Math.cos(radians) - aabb.maxX*Math.sin(radians);
				
		return new AxisAlignedBB(minX, aabb.minY, minZ, maxX, aabb.maxY, maxZ);
	}
	
	private static AxisAlignedBB rotateZ(double radians, AxisAlignedBB aabb) {
		Math.cos(radians);
		double minX = aabb.minX*Math.cos(radians) - aabb.minY*Math.sin(radians);
		double minY = aabb.minY*Math.cos(radians) - aabb.minX*Math.sin(radians);
		double maxX = aabb.maxX*Math.cos(radians) - aabb.maxY*Math.sin(radians);
		double maxY = aabb.maxY*Math.cos(radians) - aabb.maxX*Math.sin(radians);
				
		return new AxisAlignedBB(minX, minY, aabb.minZ, maxX, minY, aabb.maxZ);
	}
	
}
 