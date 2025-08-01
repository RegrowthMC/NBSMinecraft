package org.lushplugins.nbsminecraft.utils;

import net.raphimc.noteblocklib.data.MinecraftInstrument;
import net.raphimc.noteblocklib.model.instrument.Instrument;

// TODO: Implement
public class ExtendedMinecraftInstrument implements Instrument {

    private final MinecraftInstrument minecraftInstrument;
    private final int octaveShift;

    public ExtendedMinecraftInstrument(final MinecraftInstrument minecraftInstrument, final int octaveShift) {
        this.minecraftInstrument = minecraftInstrument;
        this.octaveShift = octaveShift;
    }

    public MinecraftInstrument getMinecraftInstrument() {
        return this.minecraftInstrument;
    }

    public int getOctaveShift() {
        return this.octaveShift;
    }

    @Override
    public Instrument copy() {
        return new ExtendedMinecraftInstrument(this.minecraftInstrument, this.octaveShift);
    }
}