package com.minecolonies.network.messages;

import com.minecolonies.colony.Colony;
import com.minecolonies.colony.ColonyManager;
import com.minecolonies.colony.ColonyView;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Add or Update a ColonyView on the client
 */
public class ColonyViewMessage implements IMessage, IMessageHandler<ColonyViewMessage, IMessage>
{
    private int     colonyId;
    private boolean isNewSubscription;
    private ByteBuf colonyBuffer;

    public ColonyViewMessage(){}

    /**
     * Add or Update a ColonyView on the client
     *
     * @param colony                Colony of the view to update
     * @param isNewSubscription     Boolean whether or not this is a new subscription
     */
    public ColonyViewMessage(Colony colony, boolean isNewSubscription)
    {
        this.colonyId = colony.getID();
        this.isNewSubscription = isNewSubscription;
        this.colonyBuffer = Unpooled.buffer();
        ColonyView.serializeNetworkData(colony, colonyBuffer, isNewSubscription);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(colonyId);
        buf.writeBoolean(isNewSubscription);
        buf.writeBytes(colonyBuffer);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        colonyId = buf.readInt();
        isNewSubscription = buf.readBoolean();
        colonyBuffer = buf;
    }

    @Override
    public IMessage onMessage(ColonyViewMessage message, MessageContext ctx)
    {
        return ColonyManager.handleColonyViewMessage(message.colonyId, message.colonyBuffer, message.isNewSubscription);
    }
}
