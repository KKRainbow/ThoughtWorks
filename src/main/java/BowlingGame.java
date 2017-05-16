import java.util.ArrayList;

public class BowlingGame {
	/*
	 * 把得分的字符串转换为每一次击球击倒的球的数目。
	 *
	 * @param bowlingCode 击球文本串
	 */
	private ArrayList<Integer> getScoreArray(String bowlingCode)
	{
		int curBalls = 10; //当前还未被击倒的球的数目
		ArrayList<Integer> res = new ArrayList<Integer>();
		for (int i = 0; i < bowlingCode.length(); i++)
		{
			char c = bowlingCode.charAt(i);
			switch(c)
			{
				case 'X':
				case '/':
					res.add(curBalls);
					//为了防止额外的两次击球导致无法通过|来复位curBalls，这里直接复位。
					curBalls = 10; 
					break;
				case '|': //不用单独考虑||，curBalls同会被置为10
					curBalls = 10;
					break;
				case '-':
					res.add(0);
					break;
				default:
					int downBalls = (int)c - (int)'0';
					res.add(downBalls);
					curBalls -= downBalls;
					break;
			}
		}
		return res;
	}

    public int getBowlingScore(String bowlingCode) {
		ArrayList<Integer> downArray = getScoreArray(bowlingCode);
		int weight[] = {1, 1}; //下两次击球计算分数的权重数组
		int totalScore = 0; //总分数
		int curBalls = 10; //现在还没有倒的瓶的个数
		boolean frameStart = true; //是否是此次frame的第一次击球
		int frame = 1; //当前是第几个frame

		for (int i : downArray)
		{
			//获取当前一球的权重，并移入下下次球的初始权重
			int curWeight = weight[0];
			weight[0] = weight[1];
			weight[1] = 1;

			if (frame > 10) //10个frame都已经计算完毕，不用再走后面的步骤，直接算总分。
			{
				//这里weight需要减去1，因为这次球本身是不得分的。
				totalScore += (curWeight - 1) * i;
				continue;
			}

			curBalls -= i;
			//判断是否打倒了所有球
			if (curBalls == 0)
			{
				if (frameStart)
				{
					//是Strike，后面两次击球都要额外算分
					weight[0] += 1;
					weight[1] += 1;
				}
				else
				{
					//是Spare，后面一次击球需要额外算分
					weight[0] += 1;
				}
				frameStart = true; //下一次一定是新的一个frame
			}
			else
			{
				//没有全部击倒的话，需要对frameStart做一点trick。
				frameStart = !frameStart;
				//不需要更新权重
			}

			//计算此次击球的得分。
			totalScore += curWeight * i;
			if (frameStart == true)
			{
				curBalls = 10;
				frame += 1;
			}
		}

        return totalScore;
    }
}
