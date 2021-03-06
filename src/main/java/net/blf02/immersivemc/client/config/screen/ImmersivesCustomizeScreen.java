package net.blf02.immersivemc.client.config.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blf02.immersivemc.common.config.ActiveConfig;
import net.blf02.immersivemc.common.config.ImmersiveMCConfig;
import net.blf02.immersivemc.common.config.PlacementMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.IteratableOption;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class ImmersivesCustomizeScreen extends Screen {

    protected final Screen lastScreen;
    protected OptionsRowList list;

    protected static int BUTTON_WIDTH = 256;
    protected static int BUTTON_HEIGHT = 20;


    public ImmersivesCustomizeScreen(Screen lastScreen) {
        super(new TranslationTextComponent("screen.immersivemc.immersives_customize.title"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        super.init();

        this.list = new OptionsRowList(Minecraft.getInstance(), this.width, this.height,
                32, this.height - 32, 24);

        ScreenUtils.addOption("center_brewing", ImmersiveMCConfig.autoCenterBrewing, this.list);
        ScreenUtils.addOption("center_furnace", ImmersiveMCConfig.autoCenterFurnace, this.list);
        ScreenUtils.addOption("right_click_chest", ImmersiveMCConfig.rightClickChest, this.list);
        ScreenUtils.addOption("show_placement_guide", ImmersiveMCConfig.showPlacementGuide, this.list);
        this.list.addBig(new IteratableOption(
                "config.immersivemc.placement_mode",
                (ignored, newIndex) -> {
                    ImmersiveMCConfig.itemPlacementMode.set(
                            (ImmersiveMCConfig.itemPlacementMode.get() + newIndex) % PlacementMode.values().length
                    );
                    ImmersiveMCConfig.itemPlacementMode.save();
                    ActiveConfig.loadConfigFromFile();
                },
                (ignored, option) -> new StringTextComponent(
                        I18n.get("config.immersivemc.placement_mode",
                                I18n.get("config.immersivemc.placement_mode." + ImmersiveMCConfig.itemPlacementMode.get()))
                )
        ));

        this.children.add(this.list);

        this.addButton(new Button(
                (this.width - BUTTON_WIDTH) / 2, this.height - 26,
                BUTTON_WIDTH, BUTTON_HEIGHT, new TranslationTextComponent("gui.done"),
                (button) -> this.onClose()));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);

        this.list.render(stack, mouseX, mouseY, partialTicks);

        drawCenteredString(stack, this.font, this.title.getString(),
                this.width / 2, 8, 0xFFFFFF);

        super.render(stack, mouseX, mouseY, partialTicks);

        List<IReorderingProcessor> list = SettingsScreen.tooltipAt(this.list, mouseX, mouseY);
        if (list != null) {
            this.renderTooltip(stack, list, mouseX, mouseY);
        }
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(lastScreen);
        ActiveConfig.loadConfigFromFile();
    }
}
