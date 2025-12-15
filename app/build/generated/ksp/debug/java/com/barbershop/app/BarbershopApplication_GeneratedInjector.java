package com.barbershop.app;

import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedEntryPoint;

@OriginatingElement(
    topLevelClass = BarbershopApplication.class
)
@GeneratedEntryPoint
@InstallIn(SingletonComponent.class)
public interface BarbershopApplication_GeneratedInjector {
  void injectBarbershopApplication(BarbershopApplication barbershopApplication);
}
