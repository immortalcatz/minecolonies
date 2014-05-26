package com.minecolonies.tileentities;

import com.minecolonies.entity.EntityCitizen;
import com.minecolonies.util.Utils;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class TileEntityHutWorker extends TileEntityHut
{
    private String jobName = "";

    public String getJobName()
    {
        return jobName;
    }

    public EntityCitizen createWorker()
    {
        return null;
    }

    public void bindWorker(EntityCitizen worker)
    {
        this.setHasWorker(true);
        worker.setWorkHut(this);
    }

    public void unbindWorker(EntityCitizen worker)
    {
        this.setHasWorker(false);
        worker.setWorkHut(null);
    }

    public void attemptToAddIdleCitizens(TileEntityTownHall tileEntityTownHall)
    {
        ArrayList<UUID> citizens = tileEntityTownHall.getCitizens();

        List<Entity> entityCitizens = Utils.getEntitiesFromUUID(worldObj, citizens);
        for(Entity entity : entityCitizens)
        {
            if(entity instanceof EntityCitizen)
            {
                EntityCitizen entityCitizen = (EntityCitizen) entity;
                if(entityCitizen.getJob().equals("Citizen") && !hasWorker())
                {
                    entityCitizen.addToHut(this);
                    return;
                }
            }
        }
    }

    public void removeCitizens(TileEntityTownHall tileEntityTownHall)
    {
        ArrayList<UUID> citizens = tileEntityTownHall.getCitizens();

        List<Entity> entityCitizens = Utils.getEntitiesFromUUID(worldObj, citizens);
        for(Entity entity : entityCitizens)
        {
            if(entity instanceof EntityCitizen)
            {
                EntityCitizen entityCitizen = (EntityCitizen) entity;
                if(entityCitizen.getWorkHut() == this && hasWorker())
                {
                    entityCitizen.removeFromHut(this);
                    return;
                }
            }
        }
    }
}
