package satisfyu.vinery.util.boat.impl.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import org.jetbrains.annotations.NotNull;
import satisfyu.vinery.util.boat.api.TerraformBoatType;
import satisfyu.vinery.util.boat.api.TerraformBoatTypeRegistry;
import satisfyu.vinery.util.boat.api.client.TerraformBoatClientHelper;
import satisfyu.vinery.util.boat.impl.entity.MyHolder;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class MyBoatEntityRenderer extends BoatRenderer {

	private final Map<TerraformBoatType, Pair<ResourceLocation, ListModel<Boat>>> texturesAndModels;

	public MyBoatEntityRenderer(EntityRendererProvider.Context context, boolean chest) {
		super(context, chest);

		this.texturesAndModels = TerraformBoatTypeRegistry.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getValue, entry -> {
			boolean raft = entry.getValue().isRaft();
			String prefix = raft ? (chest ? "chest_raft/" : "raft/") : (chest ? "chest_boat/" : "boat/");

			ResourceLocation id = entry.getValue().getKey();
			ResourceLocation textureId = new ResourceLocation(id.getNamespace(), "textures/entity/" + prefix + id.getPath() + ".png");

			ModelLayerLocation layer = TerraformBoatClientHelper.getLayer(id, raft, chest);
			ListModel<Boat> model = createModel(context.bakeLayer(layer), raft, chest);

			return new Pair<>(textureId, model);
		}));
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull Boat entity) {
		if (entity instanceof MyHolder) {
			TerraformBoatType boat = ((MyHolder) entity).getTerraformBoat();
			return this.texturesAndModels.get(boat).getFirst();
		}
		return super.getTextureLocation(entity);
	}

	public Pair<ResourceLocation, ListModel<Boat>> getTextureAndModel(MyHolder holder) {
		return this.texturesAndModels.get(holder.getTerraformBoat());
	}

	private ListModel<Boat> createModel(ModelPart part, boolean raft, boolean chest) {
		if (raft) {
			return chest ? new ChestRaftModel(part) : new RaftModel(part);
		} else {
			return chest ? new ChestBoatModel(part) : new BoatModel(part);
		}
	}
}