package com.example;

import net.lenni0451.classtransform.TransformerManager;
import net.minecraft.modding.api.Mod;

public class ExampleMod implements Mod {

    @Override
    public void init(TransformerManager transformerManager) throws Exception {
        System.out.println("Hello from Prcraft!");
    }

}
