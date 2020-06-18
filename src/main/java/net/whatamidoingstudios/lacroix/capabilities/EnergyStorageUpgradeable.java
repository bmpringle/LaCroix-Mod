package net.whatamidoingstudios.lacroix.capabilities;

import net.minecraftforge.energy.EnergyStorage;

public class EnergyStorageUpgradeable extends EnergyStorage {

	public EnergyStorageUpgradeable(int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
	}
	
	public boolean increaseCapacity(int newCapacity) {
		if(capacity > newCapacity) {
			return false;
		}
		capacity = newCapacity;
		return true;
	}
	
	public boolean increaseMaxRecieve(int newMaxRecieve) {
		if(maxReceive > newMaxRecieve) {
			return false;
		}
		maxReceive = newMaxRecieve;
		return true;
	}
	
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	
	public boolean increaseMaxExtract(int newMaxExtract) {
		if(maxExtract > newMaxExtract) {
			return false;
		}
		maxExtract = newMaxExtract;
		return true;
	}
}
