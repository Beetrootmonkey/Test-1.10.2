package com.beetrootmonkey.myutils.worldgen;

import java.util.Random;

import com.beetrootmonkey.myutils.block.BlockBase;
import com.beetrootmonkey.myutils.block.BlockOre;
import com.beetrootmonkey.myutils.block.ModBlocks;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class OreGen implements IWorldGenerator {

	private WorldGenerator copper_ore_overworld = new WorldGenMinable(ModBlocks.oreCopper.getDefaultState(), 8);
	private WorldGenerator copper_ore_end = new WorldGenMinable(ModBlocks.oreCopper.getDefaultState(), 8, new DefaultEndOreGenPredicate());
	private WorldGenerator copper_ore_nether = new WorldGenMinable(ModBlocks.oreCopper.getDefaultState(), 8, new DefaultNetherOreGenPredicate());
	
	private WorldGenerator test = new WorldGenLakes(Blocks.GLOWSTONE);

	private void runGenerator(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z,
			int chancesToSpawn, int minHeight, int maxHeight) {
		if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
			throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

		int heightDiff = maxHeight - minHeight + 1;
		for (int i = 0; i < chancesToSpawn; i++) {
			int x = chunk_X * 16 + rand.nextInt(16);
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunk_Z * 16 + rand.nextInt(16);
			generator.generate(world, rand, new BlockPos(x, y, z));
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		case 0: // Overworld
			this.runGenerator(copper_ore_overworld, world, random, chunkX, chunkZ, 20, 0, 64);
			this.runGenerator(test, world, random, chunkX, chunkZ, 10, 0, 100);
		case 1: // End
			this.runGenerator(copper_ore_end, world, random, chunkX, chunkZ, 20, 0, 64);
		case -1: // Nether
			this.runGenerator(copper_ore_nether, world, random, chunkX, chunkZ, 20, 0, 64);
		case 6: // Aroma Mining Dimension
			this.runGenerator(copper_ore_overworld, world, random, chunkX, chunkZ, 20, 0, 128);
		}

	}

}