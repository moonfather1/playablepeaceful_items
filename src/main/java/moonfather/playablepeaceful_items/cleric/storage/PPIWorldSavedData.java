package moonfather.playablepeaceful_items.cleric.storage;

import moonfather.playablepeaceful_items.PeacefulMod;
import moonfather.playablepeaceful_items.cleric.WanderingClericSpawning;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

public class PPIWorldSavedData extends WorldSavedData
{
	public PPIWorldSavedData(String id)
	{
		super(id);
	}



	@Override
	public void load(CompoundNBT root)
	{
		if (root.contains(PeacefulMod.MODID))
		{
//			System.out.println("!!!!!!!!   PPIWorldSavedData.load:   " + this.getId());
			CompoundNBT current = root.getCompound(PeacefulMod.MODID);
			WanderingClericSpawning.onWorldLoading(this.getId(), current.getInt("remainingSpawnDelay"));
		}
	}



	@Override
	public CompoundNBT save(CompoundNBT root)
	{
//		System.out.println("!!!!!!!!   PPIWorldSavedData.save:   " + this.getId());
		CompoundNBT c = new CompoundNBT();
		c.putInt("remainingSpawnDelay", WanderingClericSpawning.getRemainingSpawnDelay());
		root.put(PeacefulMod.MODID, c);
		return root;
	}
}
