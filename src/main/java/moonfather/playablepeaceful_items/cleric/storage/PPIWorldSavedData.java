package moonfather.playablepeaceful_items.cleric.storage;

import moonfather.playablepeaceful_items.PeacefulMod;
import moonfather.playablepeaceful_items.cleric.WanderingClericSpawning;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

public class PPIWorldSavedData extends WorldSavedData
{
	public PPIWorldSavedData()
	{
		super(PeacefulMod.MODID);
	}



	@Override
	public void load(CompoundNBT root)
	{
		if (root.contains(PeacefulMod.MODID))
		{
			CompoundNBT current = root.getCompound(PeacefulMod.MODID);
			WanderingClericSpawning.onWorldLoading(this.getId(), current.getInt("remainingSpawnDelay"));
		}
	}



	@Override
	public CompoundNBT save(CompoundNBT root)
	{
		CompoundNBT c = new CompoundNBT();
		c.putInt("remainingSpawnDelay", WanderingClericSpawning.getRemainingSpawnDelay(this.getId()));
		root.put(PeacefulMod.MODID, c);
		return root;
	}
}
