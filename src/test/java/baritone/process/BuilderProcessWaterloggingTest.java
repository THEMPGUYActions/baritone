/*
 * This file is part of Baritone.
 *
 * Baritone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Baritone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Baritone.  If not, see <https://www.gnu.org/licenses/>.
 */

package baritone.process;

import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BuilderProcessWaterloggingTest {

    @BeforeClass
    public static void bootstrapMinecraftRegistries() {
        Bootstrap.bootStrap();
    }

    @Test
    public void inventoryMatchingUsesBlockIdentityRatherThanPlacementProperties() {
        BlockState dryFence = Blocks.OAK_FENCE.defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, false);
        BlockState waterloggedFence = dryFence.setValue(BlockStateProperties.WATERLOGGED, true);

        assertTrue(BuilderProcess.hasBlockItem(dryFence, waterloggedFence));
        assertFalse(BuilderProcess.hasBlockItem(dryFence, Blocks.OAK_PLANKS.defaultBlockState()));
    }

    @Test
    public void waterloggingOnlyMatchesWhenEveryOtherPropertyMatches() {
        BlockState dryFence = Blocks.OAK_FENCE.defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, false);
        BlockState waterloggedFence = dryFence.setValue(BlockStateProperties.WATERLOGGED, true);
        BlockState connectedDryFence = dryFence.setValue(BlockStateProperties.EAST, true);

        assertTrue(BuilderProcess.needsWaterlogging(dryFence, waterloggedFence));
        assertFalse(BuilderProcess.needsWaterlogging(waterloggedFence, dryFence));
        assertFalse(BuilderProcess.needsWaterlogging(dryFence, dryFence));
        assertFalse(BuilderProcess.needsWaterlogging(connectedDryFence, waterloggedFence));
        assertFalse(BuilderProcess.needsWaterlogging(dryFence, Blocks.OAK_PLANKS.defaultBlockState()));
    }
}
