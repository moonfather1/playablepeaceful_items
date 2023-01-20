package moonfather.playablepeaceful_items;

import moonfather.playablepeaceful_items.cleric.WanderingClericInitClientOnly;
import moonfather.playablepeaceful_items.cotton.CottonInitClientOnly;
import moonfather.playablepeaceful_items.gunpowder.blocks.SulphureousLilypadInitClientOnly;
import moonfather.playablepeaceful_items.gunpowder.sprite.SpriteInitClientOnly;
import moonfather.playablepeaceful_items.membrane.PhantomBushInitClientOnly;
import moonfather.playablepeaceful_items.slimeball.SlimeInitClientOnly;
import net.minecraftforge.eventbus.api.IEventBus;

public class ClientInitializationHandler
{
	private final IEventBus eventBus;

	public ClientInitializationHandler(IEventBus eventBus)
	{
		this.eventBus = eventBus;
	}

	public void registerClientOnlyEvents()
	{
		eventBus.register(CottonInitClientOnly.class);
		eventBus.register(PhantomBushInitClientOnly.class);
		eventBus.register(SlimeInitClientOnly.class);
		eventBus.register(WanderingClericInitClientOnly.class);
		eventBus.register(SpriteInitClientOnly.class);
		eventBus.register(SulphureousLilypadInitClientOnly.class);
		//this was unnecessary EventBusSubscriber has side parameter (called "value")
	}
}
