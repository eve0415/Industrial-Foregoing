/*
 * This file is part of Industrial Foregoing.
 *
 * Copyright 2019, Buuz135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.buuz135.industrial.utils;

import java.util.ArrayList;
import java.util.List;
import com.buuz135.industrial.proxy.block.Cuboid;
import com.buuz135.industrial.proxy.block.DistanceRayTraceResult;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RayTraceUtils {

    public static RayTraceResult rayTraceBoxesClosest(final Vec3d start, final Vec3d end,
            final BlockPos pos, final List<Cuboid> boxes) {
        final List<DistanceRayTraceResult> results = new ArrayList<>();
        for (final Cuboid box : boxes) {
            final DistanceRayTraceResult hit = rayTraceBox(pos, start, end, box);
            if (hit != null)
                results.add(hit);
        }
        RayTraceResult closestHit = null;
        double curClosest = Double.MAX_VALUE;
        for (final DistanceRayTraceResult hit : results) {
            if (curClosest > hit.dist) {
                closestHit = hit;
                curClosest = hit.dist;
            }
        }
        return closestHit;
    }

    public static DistanceRayTraceResult rayTraceBox(final BlockPos pos, final Vec3d start,
            final Vec3d end, final Cuboid box) {
        final Vec3d startRay = start.subtract(new Vec3d(pos));
        final Vec3d endRay = end.subtract(new Vec3d(pos));
        final RayTraceResult bbResult = box.aabb().calculateIntercept(startRay, endRay);

        if (bbResult != null) {
            final Vec3d hitVec = bbResult.hitVec.add(new Vec3d(pos));
            final EnumFacing sideHit = bbResult.sideHit;
            final double dist = start.squareDistanceTo(hitVec);
            return new DistanceRayTraceResult(hitVec, pos, sideHit, box, dist);
        }
        return null;
    }

    public static RayTraceResult rayTrace(final IBlockState state, final IBlockAccess world,
            final BlockPos pos, final EntityPlayer player, final double distance,
            final List<Cuboid> boundingBoxes) {
        final Vec3d vec3d = player.getPositionEyes(0);
        final Vec3d vec3d1 = player.getLook(0);
        final Vec3d vec3d2 =
                vec3d.add(vec3d1.x * distance, vec3d1.y * distance, vec3d1.z * distance);
        return rayTraceBoxesClosest(vec3d, vec3d2, pos, boundingBoxes);
    }

    public static RayTraceResult rayTraceSimple(final World world, final EntityLivingBase living,
            final double blockReachDistance, final float partialTicks) {
        final Vec3d vec3d = living.getPositionEyes(partialTicks);
        final Vec3d vec3d1 = living.getLook(partialTicks);
        final Vec3d vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance,
                vec3d1.z * blockReachDistance);
        return world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
    }

}
