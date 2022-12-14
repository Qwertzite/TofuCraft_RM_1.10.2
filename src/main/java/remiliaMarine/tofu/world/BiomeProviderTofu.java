package remiliaMarine.tofu.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.storage.WorldInfo;
import remiliaMarine.tofu.init.TcBiomes;
import remiliaMarine.tofu.world.biome.BiomeTofu;
import remiliaMarine.tofu.world.gen.layer.GenLayerTofu;

public class BiomeProviderTofu extends BiomeProvider {
	
    public static ArrayList<Biome> allowedBiomes = new ArrayList<Biome>(Arrays.asList(
            TcBiomes.TOFU_PLAINS,
            TcBiomes.TOFU_FOREST,
            TcBiomes.TOFU_BUILDINGS,
            TcBiomes.TOFU_EXTREME_HILLS));
    
    private GenLayer genBiomes;

    /** The BiomeCache object for this world. */
    private BiomeCache biomeCache;
    /** A list of biomes that the player can spawn in. */
    private final List<Biome> biomesToSpawnIn;
	
    protected BiomeProviderTofu()
    {
        this.biomeCache = new BiomeCache(this);
        this.biomesToSpawnIn = Lists.newArrayList(allowedBiomes);
    }
    
    public BiomeProviderTofu(long seed, WorldType worldType)
    {
        this();
        GenLayer agenlayer = GenLayerTofu.initializeAllBiomeGeneratorsTofu(seed, worldType);
        this.genBiomes = agenlayer;
    }
    
    public BiomeProviderTofu(WorldInfo info)
    {
        this(info.getSeed(), info.getTerrainType());
    }
    
    /**
     * Gets the list of valid biomes for the player to spawn in.
     */
    @Override
    public List<Biome> getBiomesToSpawnIn()
    {
        return this.biomesToSpawnIn;
    }
    
    @Override
    public Biome getBiome(BlockPos pos, Biome defaultBiome)
    {
        return this.biomeCache.getBiome(pos.getX(), pos.getZ(), defaultBiome);
    }
    
    @Override
    public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height)
    {
        IntCache.resetIntCache();

        if (biomes == null || biomes.length < width * height)
        {
            biomes = new Biome[width * height];
        }

        int[] aint = this.genBiomes.getInts(x, z, width, height);

        for (int i1 = 0; i1 < width * height; ++i1)
        {
            biomes[i1] = BiomeTofu.REGISTRY.getObjectById(aint[i1]);
        }

        return biomes;
    }
    
    /**
     * Gets a list of biomes for the specified blocks.
     */
    @Override
    public Biome[] getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
    {
        IntCache.resetIntCache();

        if (listToReuse == null || listToReuse.length < width * length)
        {
            listToReuse = new Biome[width * length];
        }

        if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0)
        {
            Biome[] abiome = this.biomeCache.getCachedBiomes(x, z);
            System.arraycopy(abiome, 0, listToReuse, 0, width * length);
            return listToReuse;
        }
        else
        {
            int[] aint = this.genBiomes.getInts(x, z, width, length);

            for (int i = 0; i < width * length; ++i)
            {
                listToReuse[i] = BiomeTofu.REGISTRY.getObjectById(aint[i]);
            }

            return listToReuse;
        }
    }
    
    /**
     * checks given Chunk's Biomes against List of allowed ones
     */
    @Override
    public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed)
    {
        IntCache.resetIntCache();
        int l = x - radius >> 2;
        int i1 = z - radius >> 2;
        int j1 = x + radius >> 2;
        int k1 = z + radius >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
        int[] aint = this.genBiomes.getInts(l, i1, l1, i2);

        for (int j2 = 0; j2 < l1 * i2; ++j2)
        {
            Biome biomeGenTofuBase = BiomeTofu.REGISTRY.getObjectById(aint[j2]);

            if (!allowed.contains(biomeGenTofuBase))
            {
                return false;
            }
        }

        return true;
    }
    
    @Nullable
    @Override
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random)
    {
        IntCache.resetIntCache();
        int i = x - range >> 2;
        int j = z - range >> 2;
        int k = x + range >> 2;
        int l = z + range >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        int[] aint = this.genBiomes.getInts(i, j, i1, j1);
        BlockPos blockpos = null;
        int k1 = 0;

        for (int l1 = 0; l1 < i1 * j1; ++l1)
        {
            int i2 = i + l1 % i1 << 2;
            int j2 = j + l1 / i1 << 2;
            Biome biome = Biome.REGISTRY.getObjectById(aint[l1]);

            if (biomes.contains(biome) && (blockpos == null || random.nextInt(k1 + 1) == 0))
            {
                blockpos = new BlockPos(i2, 0, j2);
                ++k1;
            }
        }

        return blockpos;
    }
    
    /**
     * Calls the WorldChunkManager's biomeCache.cleanupCache()
     */
    public void cleanupCache()
    {
        this.biomeCache.cleanupCache();
    }
    
    @Override
    public GenLayer[] getModdedBiomeGenerators(WorldType worldType, long seed, GenLayer[] original)
    {
        return original;
    }
}
