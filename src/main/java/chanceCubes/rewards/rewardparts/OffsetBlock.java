package chanceCubes.rewards.rewardparts;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import chanceCubes.blocks.BlockFallingCustom;
import chanceCubes.config.CCubesSettings;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class OffsetBlock
{
	protected boolean relativeToPlayer = false;
	public int xOff;
	public int yOff;
	public int zOff;
	
	protected byte data = 0;

	protected boolean falling;
	protected int delay = 0;

	protected Block block;

	public OffsetBlock(int x, int y, int z, Block b, boolean falling)
	{
		this.xOff = x;
		this.yOff = y;
		this.zOff = z;
		this.block = b;
		this.falling = falling;
	}
	
	public OffsetBlock(int x, int y, int z, Block b, boolean falling, int delay)
	{
		this.xOff = x;
		this.yOff = y;
		this.zOff = z;
		this.block = b;
		this.falling = falling;
		this.delay = delay;
	}

	public void spawnInWorld(final World world, final int x, final int y, final int z)
	{
		if(!falling)
		{
			this.placeInWorld(world, x, y, z, true);
		}
		else
		{
			if(delay != 0)
			{
				Task task = new Task("Falling_Block_At_(" + xOff + "," + yOff + "," + zOff + ")", delay)
				{
					@Override
					public void callback()
					{
						spawnFallingBlock(world, x, y, z);
					}
				};
				Scheduler.scheduleTask(task);
			}
			else
			{
				spawnFallingBlock(world, x, y, z);
			}
		}
	}

	protected void spawnFallingBlock(World world, int x, int y, int z)
	{
		double yy = (((double) (y + yOff + CCubesSettings.dropHeight)) + 0.5) >= 256 ? 255 : (((double) (y + yOff + CCubesSettings.dropHeight)) + 0.5);
		for(int yyy = (int) yy; yyy >= y + yOff; yyy--)
			world.setBlockToAir((x + xOff), yyy, (z + zOff));
		BlockFallingCustom entityfallingblock = new BlockFallingCustom(world, ((double) (x + xOff)) + 0.5, yy, ((double) (z + zOff)) + 0.5, block, data, y + yOff, this);
		world.spawnEntityInWorld(entityfallingblock);
	}

	public void setDealy(int delay)
	{
		this.delay = delay;
	}
	
	public void setData(byte d)
	{
		this.data = d;
	}
	
	public OffsetBlock setRelativeToPlayer(boolean relative)
	{
		this.relativeToPlayer = relative;
		return this;
	}
	
	public boolean isRelativeToPlayer()
	{
		return this.relativeToPlayer;
	}
	
	public void placeInWorld(World world, int x, int y, int z, boolean offset)
	{
		if(offset)
			world.setBlock(x + xOff, y + yOff, z + zOff, block, data, 2);
		else
			world.setBlock(x, y, z, block, data, 2);
		
	}
}