plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-common")
}

dependencies {
    modCompileOnlyApi(libs.puzzleslib.common)
}

multiloader {
    mixins {
        mixin(
            "ChunkAccess\u0024PackedTicksMixin",
            "LeavesBlockMixin",
            "LevelChunkMixin",
            "SerializableChunkDataMixin",
            "ServerLevelMixin"
        )
    }
}
