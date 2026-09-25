package sirskoi.simplesteeds.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import sirskoi.simplesteeds.modconfig;

public class modmenuconfig extends Screen {
    private final modconfig defaults = new modconfig();
    private final List<RowEntry> rows = new ArrayList<>();
    private double scrollOffset = 0;
    private double maxScroll = 0;

    public modmenuconfig(Screen ignoredParent) {
        super(Component.literal("Simple Steeds"));
    }

    @Override
    protected void init() {
        clearWidgets();
        rows.clear();

        int centerX = this.width / 2;
        int leftX = centerX - 155;
        int rightX = centerX + 5;
        int widgetWidth = 150;
        int currentY = 38;

        modconfig cfg = modconfig.INSTANCE;
        boolean isLocked = cfg.isServerConfig;

        // warn player if config is locked on server sync
        if (isLocked) {
            addCategoryHeader(centerX, currentY, "Config Locked By Server");
            currentY += 24;
        }

        //general
        addCategoryHeader(centerX, currentY, "General");
        currentY += 22;

        CycleButton<Boolean> lootToggle = addBooleanRow(leftX, rightX, currentY, widgetWidth, "Loot Table Changes", cfg.enableLootTableChanges,
                val -> cfg.enableLootTableChanges = val, "Add modded enchantment books to world loot chests");
        currentY += 22;

        CycleButton<Boolean> enchantToggle = addBooleanRow(leftX, rightX, currentY, widgetWidth, "Mount Enchantments", cfg.enableVanillaMountEnchantments,
                val -> cfg.enableVanillaMountEnchantments = val, "Allow vanilla enchantments on mount gear");
        currentY += 24;

        Button resetGen = addResetButton(centerX, currentY, "Reset General Defaults", () -> {
            cfg.enableLootTableChanges = defaults.enableLootTableChanges;
            cfg.enableVanillaMountEnchantments = defaults.enableVanillaMountEnchantments;
            lootToggle.setValue(defaults.enableLootTableChanges);
            enchantToggle.setValue(defaults.enableVanillaMountEnchantments);
        });
        currentY += 40;

        //movement
        addCategoryHeader(centerX, currentY, "Movement");
        currentY += 22;

        ConfigSlider momentumSlider = addSliderRow(leftX, rightX, currentY, widgetWidth, "Momentum Speed", cfg.momentumSpeedMultiplier,
                2.0, 0.05, "%.2f", val -> cfg.momentumSpeedMultiplier = val, "Speed bonus per momentum level");
        currentY += 22;

        ConfigSlider leapSlider = addSliderRow(leftX, rightX, currentY, widgetWidth, "Leaping Jump", cfg.leapingJumpMultiplier,
                2.0, 0.05, "%.2f", val -> cfg.leapingJumpMultiplier = val, "Jump bonus per leaping level");
        currentY += 22;

        ConfigSlider ghastSlider = addSliderRow(leftX, rightX, currentY, widgetWidth, "Happy Ghast Speed", cfg.happyGhastSpeedMultiplier,
                2.0, 0.05, "%.2f", val -> cfg.happyGhastSpeedMultiplier = val, "Speed multiplier when riding happy ghasts");
        currentY += 22;

        ConfigSlider soulSlider = addSliderRow(leftX, rightX, currentY, widgetWidth, "Soul Speed Bonus", cfg.soulSpeedBonusPerLevel,
                1.0, 0.01, "%.2f", val -> cfg.soulSpeedBonusPerLevel = val, "Speed bonus on soul blocks per soul speed level");
        currentY += 24;

        Button resetMov = addResetButton(centerX, currentY, "Reset Movement Defaults", () -> {
            cfg.momentumSpeedMultiplier = defaults.momentumSpeedMultiplier;
            cfg.leapingJumpMultiplier = defaults.leapingJumpMultiplier;
            cfg.happyGhastSpeedMultiplier = defaults.happyGhastSpeedMultiplier;
            cfg.soulSpeedBonusPerLevel = defaults.soulSpeedBonusPerLevel;

            momentumSlider.setValueDirect(defaults.momentumSpeedMultiplier);
            leapSlider.setValueDirect(defaults.leapingJumpMultiplier);
            ghastSlider.setValueDirect(defaults.happyGhastSpeedMultiplier);
            soulSlider.setValueDirect(defaults.soulSpeedBonusPerLevel);
        });
        currentY += 40;

        //prot
        addCategoryHeader(centerX, currentY, "Protection");
        currentY += 22;

        ConfigSlider featherSlider = addSliderRow(leftX, rightX, currentY, widgetWidth, "Feather Falling", cfg.featherFallingReductionPerLevel,
                20.0, 0.5, "%.1f", val -> cfg.featherFallingReductionPerLevel = val, "Damage absorbed per feather falling level (6.0 = 3 hearts)");
        currentY += 22;

        ConfigSlider protSlider = addSliderRow(leftX, rightX, currentY, widgetWidth, "Protection", cfg.protectionReductionPerLevel,
                1.0, 0.025, "%.3f", val -> cfg.protectionReductionPerLevel = val, "Damage reduction per protection level (0.125 = 12.5%)");
        currentY += 22;

        ConfigSlider fireSlider = addSliderRow(leftX, rightX, currentY, widgetWidth, "Fire Protection", cfg.fireProtectionReductionPerLevel,
                1.0, 0.05, "%.2f", val -> cfg.fireProtectionReductionPerLevel = val, "Damage reduction per fire protection level (0.2 = 20%)");
        currentY += 22;

        ConfigSlider blastSlider = addSliderRow(leftX, rightX, currentY, widgetWidth, "Blast Protection", cfg.blastProtectionReductionPerLevel,
                1.0, 0.05, "%.2f", val -> cfg.blastProtectionReductionPerLevel = val, "Damage reduction per blast protection level (0.2 = 20%)");
        currentY += 22;

        ConfigSlider projSlider = addSliderRow(leftX, rightX, currentY, widgetWidth, "Projectile Prot", cfg.projectileProtectionReductionPerLevel,
                1.0, 0.05, "%.2f", val -> cfg.projectileProtectionReductionPerLevel = val, "Damage reduction per projectile protection level (0.2 = 20%)");
        currentY += 24;

        Button resetProt = addResetButton(centerX, currentY, "Reset Protection Defaults", () -> {
            cfg.featherFallingReductionPerLevel = defaults.featherFallingReductionPerLevel;
            cfg.protectionReductionPerLevel = defaults.protectionReductionPerLevel;
            cfg.fireProtectionReductionPerLevel = defaults.fireProtectionReductionPerLevel;
            cfg.blastProtectionReductionPerLevel = defaults.blastProtectionReductionPerLevel;
            cfg.projectileProtectionReductionPerLevel = defaults.projectileProtectionReductionPerLevel;

            featherSlider.setValueDirect(defaults.featherFallingReductionPerLevel);
            protSlider.setValueDirect(defaults.protectionReductionPerLevel);
            fireSlider.setValueDirect(defaults.fireProtectionReductionPerLevel);
            blastSlider.setValueDirect(defaults.blastProtectionReductionPerLevel);
            projSlider.setValueDirect(defaults.projectileProtectionReductionPerLevel);
        });
        currentY += 24;

        this.maxScroll = Math.max(0, currentY - (this.height - 42));

        //scrollbar
        int scrollbarX = centerX + 162;
        int topArrowY = 36;
        int bottomArrowY = this.height - 48;

        addRenderableWidget(Button.builder(Component.literal("▲"), ignored -> setScroll(this.scrollOffset - 32))
                .bounds(scrollbarX, topArrowY, 18, 18).build());
        addRenderableWidget(Button.builder(Component.literal("▼"), ignored -> setScroll(this.scrollOffset + 32))
                .bounds(scrollbarX, bottomArrowY, 18, 18).build());

        //centerbuttons
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, ignored -> this.onClose())
                .bounds(centerX - 100, this.height - 26, 200, 20).build());

        // lock controls while synced
        if (isLocked) {
            for (RowEntry entry : rows) {
                if (entry.control != null) {
                    entry.control.active = false;
                }
            }
            resetGen.active = false;
            resetMov.active = false;
            resetProt.active = false;
        }

        updatePositions();
    }

    private void addCategoryHeader(int centerX, int y, String title) {
        StringWidget header = new StringWidget(Component.literal("§e§l" + title), this.font);
        int textWidth = this.font.width(header.getMessage());
        header.setPosition(centerX - (textWidth / 2), y);
        addRenderableWidget(header);
        rows.add(new RowEntry(y, null, header));
    }

    private CycleButton<Boolean> addBooleanRow(int leftX, int rightX, int y, int width, String label, boolean initialVal,
                                               java.util.function.Consumer<Boolean> consumer, String tooltip) {
        Button labelBtn = Button.builder(Component.literal("§f" + label), ignored -> {})
                .bounds(leftX, y, width, 20).build();
        labelBtn.active = false;
        labelBtn.setTooltip(Tooltip.create(Component.literal(tooltip)));
        addRenderableWidget(labelBtn);

        CycleButton<Boolean> toggle = CycleButton.onOffBuilder(initialVal)
                .displayOnlyValue()
                .create(rightX, y, width, 20, Component.empty(), (ignored, val) -> consumer.accept(val));
        toggle.setTooltip(Tooltip.create(Component.literal(tooltip)));
        addRenderableWidget(toggle);

        rows.add(new RowEntry(y, labelBtn, toggle));
        return toggle;
    }

    private ConfigSlider addSliderRow(int leftX, int rightX, int y, int width, String label, float initialVal,
                                      double max, double step, String format,
                                      java.util.function.Consumer<Float> consumer, String tooltip) {
        Button labelBtn = Button.builder(Component.literal("§f" + label), ignored -> {})
                .bounds(leftX, y, width, 20).build();
        labelBtn.active = false;
        labelBtn.setTooltip(Tooltip.create(Component.literal(tooltip)));
        addRenderableWidget(labelBtn);

        ConfigSlider slider = new ConfigSlider(rightX, y, width, 20, initialVal, 0.0, max, step, format, consumer);
        slider.setTooltip(Tooltip.create(Component.literal(tooltip)));
        addRenderableWidget(slider);

        rows.add(new RowEntry(y, labelBtn, slider));
        return slider;
    }

    private Button addResetButton(int centerX, int y, String label, Runnable onReset) {
        Button resetBtn = Button.builder(Component.literal("§f" + label), ignored -> onReset.run())
                .bounds(centerX - 100, y, 200, 20).build();
        resetBtn.setTooltip(Tooltip.create(Component.literal("Reset this section to default configuration values")));
        addRenderableWidget(resetBtn);
        rows.add(new RowEntry(y, null, resetBtn));
        return resetBtn; // returned to allow disabling by lock
    }

    private void setScroll(double target) {
        this.scrollOffset = Math.clamp(target, 0.0, this.maxScroll);
        updatePositions();
    }

    private void updatePositions() {
        for (RowEntry entry : rows) {
            int currentY = (int) (entry.baseY - this.scrollOffset);
            boolean isVisible = currentY >= 32 && currentY <= (this.height - 50);

            if (entry.label != null) {
                entry.label.setY(currentY);
                entry.label.visible = isVisible;
            }
            if (entry.control != null) {
                entry.control.setY(currentY);
                entry.control.visible = isVisible;
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        setScroll(this.scrollOffset - (verticalAmount * 24.0));
        return true;
    }

    @Override
    public void onClose() {
        modconfig.save();
        super.onClose();
    }

    private static class RowEntry {
        final int baseY;
        final AbstractWidget label;
        final AbstractWidget control;

        RowEntry(int baseY, AbstractWidget label, AbstractWidget control) {
            this.baseY = baseY;
            this.label = label;
            this.control = control;
        }
    }

    public static class ConfigSlider extends AbstractSliderButton {
        private final double min;
        private final double max;
        private final double step;
        private final String format;
        private final java.util.function.Consumer<Float> consumer;

        public ConfigSlider(int x, int y, int width, int height, double value,
                            double min, double max, double step, String format,
                            java.util.function.Consumer<Float> consumer) {
            super(x, y, width, height, Component.empty(), (value - min) / (max - min));
            this.min = min;
            this.max = max;
            this.step = step;
            this.format = format;
            this.consumer = consumer;
            this.updateMessage();
        }

        public void setValueDirect(double newValue) {
            this.value = Math.clamp((newValue - this.min) / (this.max - this.min), 0.0, 1.0);
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            double val = getActualValue();
            this.setMessage(Component.literal(String.format(Locale.ROOT, format, val)));
        }

        @Override
        protected void applyValue() {
            consumer.accept((float) getActualValue());
        }

        private double getActualValue() {
            double val = this.min + this.value * (this.max - this.min);
            if (this.step > 0) {
                val = Math.round(val / this.step) * this.step;
            }
            return val;
        }
    }
}