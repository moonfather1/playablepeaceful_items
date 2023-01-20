package moonfather.playablepeaceful_items.cleric.storage;

import moonfather.playablepeaceful_items.Constants;
import moonfather.playablepeaceful_items.cleric.WanderingClericSpawning;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class PPIWorldSavedData extends SavedData
{
	public PPIWorldSavedData()
	{
		super();
	}

	public PPIWorldSavedData(CompoundTag root)  // load method is now this
	{
		if (root.contains(Constants.MODID))
		{
			//System.out.println("!!!!!!!!   PPIWorldSavedData.load:   " + this.getId());
			CompoundTag current = root.getCompound(Constants.MODID);
			WanderingClericSpawning.onWorldLoading("NOLONGERUSINGIDS", current.getInt("remainingSpawnDelay"));
		}
	}



	@Override
	public CompoundTag save(CompoundTag root)
	{
//		System.out.println("!!!!!!!!   PPIWorldSavedData.save:   " + this.getId());
		CompoundTag c = new CompoundTag();
		c.putInt("remainingSpawnDelay", WanderingClericSpawning.getRemainingSpawnDelay());
		root.put(Constants.MODID, c);
		return root;
	}
}
