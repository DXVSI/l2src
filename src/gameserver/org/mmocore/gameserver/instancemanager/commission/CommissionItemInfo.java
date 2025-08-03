package org.mmocore.gameserver.instancemanager.commission;

import org.mmocore.gameserver.model.items.ItemInstance;
import org.mmocore.gameserver.templates.item.ExItemType;

/**
 * @author : Darvin
 *         <p/>
 *         Вероятно, в будущем будет наследоваться от {@link org.mmocore.gameserver.model.items.ItemInfo} либо от
 *         {@link org.mmocore.gameserver.model.items.TradeItem} (На данный момент клиент это не поддерживает). Пакет, в котором используется этот
 *         класс-оболочка {@link org.mmocore.gameserver.network.serverpackets.ExResponseCommissionList}
 */
public class CommissionItemInfo
{
	private long auctionId;
	private long registeredPrice;
	private ExItemType exItemType;
	private int saleDays;
	private long saleEndTime;
	private int sellerId;
	private String sellerName;
	private ItemInstance item;

	public CommissionItemInfo(ItemInstance item)
	{
		this.item = item;
	}

	public long getAuctionId()
	{
		return auctionId;
	}

	public long getRegisteredPrice()
	{
		return registeredPrice;
	}

	public ExItemType getExItemType()
	{
		return exItemType;
	}

	public int getSaleDays()
	{
		return saleDays;
	}

	public long getSaleEndTime()
	{
		return saleEndTime;
	}
	
	public int getSellerId()
	{
		return sellerId;
	}

	public String getSellerName()
	{
		return sellerName;
	}

	public ItemInstance getItem()
	{
		return item;
	}

	public void setAuctionId(long auctionId)
	{
		this.auctionId = auctionId;
	}

	public void setRegisteredPrice(long registeredPrice)
	{
		this.registeredPrice = registeredPrice;
	}

	public void setExItemType(ExItemType exItemType)
	{
		this.exItemType = exItemType;
	}

	public void setSaleDays(int saleDays)
	{
		this.saleDays = saleDays;
	}

	public void setSaleEndTime(long saleEndTime)
	{
		this.saleEndTime = saleEndTime;
	}

	public void setSellerId(int sellerId)
	{
		this.sellerId = sellerId;
	}

	public void setSellerName(String sellerName)
	{
		this.sellerName = sellerName;
	}
}
