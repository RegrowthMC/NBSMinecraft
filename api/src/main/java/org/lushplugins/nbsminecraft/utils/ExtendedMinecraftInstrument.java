package org.lushplugins.nbsminecraft.utils;

import net.raphimc.noteblocklib.data.MinecraftInstrument;
import net.raphimc.noteblocklib.model.instrument.Instrument;

public record ExtendedMinecraftInstrument(MinecraftInstrument minecraftInstrument, int octaveShift) implements Instrument {

    public String soundName() {
        return this.minecraftInstrument.mcSoundName() + "_" + this.octaveShift;
    }

    @Override
    public Instrument copy() {
        return new ExtendedMinecraftInstrument(this.minecraftInstrument, this.octaveShift);
    }
}