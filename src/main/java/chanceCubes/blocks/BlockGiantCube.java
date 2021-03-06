package chanceCubes.blocks;

import java.util.Random;

import chanceCubes.items.CCubesItems;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.CCubesAchievements;
import chanceCubes.util.GiantCubeUtil;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGiantCube extends BaseChanceBlock implements ITileEntityProvider
{
	public BlockGiantCube()
	{
		super("giant_Chance_Cube");
		super.setCreativeTab(null);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileGiantCube();
	}

	@Override
	public int quantityDropped(Random rand)
	{
		return 0;
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		if(!world.isRemote && player != null && !(player instanceof FakePlayer))
		{
			TileGiantCube te = (TileGiantCube) world.getTileEntity(pos);

			if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
			{
				CCubesBlocks.COMPACT_GIANT_CUBE.dropBlockAsItem(world, pos, CCubesBlocks.COMPACT_GIANT_CUBE.getDefaultState(), 1);
				GiantCubeUtil.removeStructure(te.getMasterPostion(), world);
				return true;
			}

			if(te != null)
			{
				if(!te.hasMaster() || !(world.getTileEntity(te.getMasterPostion()) instanceof TileGiantCube))
				{
					world.setBlockToAir(pos);
					return false;
				}
				player.addStat(CCubesAchievements.GiantChanceCube);
				GiantCubeRegistry.INSTANCE.triggerRandomReward(world, te.getMasterPostion(), player, 0);
				GiantCubeUtil.removeStructure(te.getMasterPostion(), world);
			}
		}
		return true;
	}
	
    public float getExplosionResistance(Entity exploder)
    {
    	return Float.MAX_VALUE;
    }
}