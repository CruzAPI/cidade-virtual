package com.eul4;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Price
{
	private int likes;
	private int dislikes;
	
	public Price(int price)
	{
		this.likes = price;
		this.dislikes = price;
	}
	
	public boolean isFree()
	{
		return likes == 0 && dislikes == 0;
	}
}
