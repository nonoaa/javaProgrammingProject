package corona;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GetAPI {

	public GetAPI() throws Exception {
		Scanner sc = new Scanner(System.in);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(readUrl());
		JSONArray jsonArray = (JSONArray) jsonObject.get("stores");
		int inputNum;

		while (jsonArray.size() == 0) {
			System.out.print("주소를 잘못 입력하셨습니다.\n다시 ");
			jsonObject = (JSONObject) jsonParser.parse(readUrl());
			jsonArray = (JSONArray) jsonObject.get("stores");
		}

		
		while (true) {
			System.out.print("\n번호를 입력하세요.\n1.지역내 전체가게출력\n2.가게이름검색\n3.마스크재고가 100개 이상인 가게 출력\n4.다른지역 검색\n--->>");
			inputNum = sc.nextInt();
			switch (inputNum) {
			case 1:
				printAll(jsonArray);
				break;
			case 2:
				printByName(jsonArray);
				break;
			case 3:
				printPlenty(jsonArray);
				break;
			case 4:
				jsonObject = (JSONObject) jsonParser.parse(readUrl());
				jsonArray = (JSONArray) jsonObject.get("stores");
				break;
			default:
				System.out.println("잘못된 값을 입력하셨습니다.");
				break;
			}
		}
	}

	private static String readUrl() throws Exception {
		Scanner sc = new Scanner(System.in);
		BufferedReader reader = null;
		try {
			System.out.print("주소를 입력하세요(ex:OO도 OO시 OO구, 서울특별시 OO구)\n--->>");
			String input = sc.nextLine();
			System.out.println("입력한 주소 : " + input);

			String encodeResult = URLEncoder.encode(input, "UTF-8");

			URL url = new URL(
					"https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/storesByAddr/json?address=" + encodeResult);
			reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String str;

			while ((str = reader.readLine()) != null) {
				buffer.append(str);
			}

			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	void printAll(JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject entity = (JSONObject) jsonArray.get(i);
			String storeAddr = (String) entity.get("addr");
			String storeName = (String) entity.get("name");
			String storeCreated = (String) entity.get("created_at");
			System.out.println(i + 1);
			System.out.println("가게 이름: " + storeName);
			System.out.println("주소: " + storeAddr);
			String storeRemain = (String) entity.get("remain_stat");
			if (storeRemain == null)
				System.out.println("현재 마스크 재고: 알 수 없음");
			else if (storeRemain.equals("plenty"))
				System.out.println("현재 마스크 재고: 100개 이상");
			else if (storeRemain.equals("some"))
				System.out.println("현재 마스크 재고: 30개 이상 100개 미만");
			else if (storeRemain.equals("few"))
				System.out.println("현재 마스크 재고: 2개 이상 30개 미만");
			else if (storeRemain.equals("empty"))
				System.out.println("현재 마스크 재고: 없음");
			else if (storeRemain.equals("break"))
				System.out.println("현재 마스크 재고: 판매중지 상태");

			if (storeCreated == null)
				System.out.println("데이터 업데이트 날짜: 알 수 없음");
			else {
				System.out.println("데이터 업데이트 날짜: " + storeCreated);
			}
			System.out.println("-----------------------------------");
		}
	}

	void printByName(JSONArray jsonArray) {

		Scanner sc = new Scanner(System.in);
		System.out.print("가게 이름을 입력하세요.\n--->>");

		String input = sc.nextLine();
		boolean check = false;

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject entity = (JSONObject) jsonArray.get(i);
			String storeAddr = (String) entity.get("addr");
			String storeName = (String) entity.get("name");
			String storeCreated = (String) entity.get("created_at");

			if (storeName.equals(input)) {
//				System.out.println(i + 1);
				System.out.println("가게 이름: " + storeName);
				System.out.println("주소: " + storeAddr);
				String storeRemain = (String) entity.get("remain_stat");
				if (storeRemain == null)
					System.out.println("현재 마스크 재고: 알 수 없음");
				else if (storeRemain.equals("plenty"))
					System.out.println("현재 마스크 재고: 100개 이상");
				else if (storeRemain.equals("some"))
					System.out.println("현재 마스크 재고: 30개 이상 100개 미만");
				else if (storeRemain.equals("few"))
					System.out.println("현재 마스크 재고: 2개 이상 30개 미만");
				else if (storeRemain.equals("empty"))
					System.out.println("현재 마스크 재고: 없음");
				else if (storeRemain.equals("break"))
					System.out.println("현재 마스크 재고: 판매중지 상태");

				if (storeCreated == null)
					System.out.println("데이터 업데이트 날짜: 알 수 없음");
				else {
					System.out.println("데이터 업데이트 날짜: " + storeCreated);
				}
				System.out.println("-----------------------------------");
				check = true;
			}

		}
		if (check == false) {
			System.out.println("검색하신 가게가 존재하지 않습니다.");
			System.out.println("-----------------------------------");
		}
	}

	void printPlenty(JSONArray jsonArray) {
		int cnt = 0;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject entity = (JSONObject) jsonArray.get(i);
			String storeAddr = (String) entity.get("addr");
			String storeName = (String) entity.get("name");
			String storeCreated = (String) entity.get("created_at");
			String storeRemain = (String) entity.get("remain_stat");

			if (storeRemain == null)
				continue;
			else if (storeRemain.equals("plenty")) {
				System.out.println(++cnt);
				System.out.println("가게 이름: " + storeName);
				System.out.println("주소: " + storeAddr);
				System.out.println("현재 마스크 재고: 100개 이상");

				if (storeCreated == null)
					System.out.println("데이터 업데이트 날짜: 알 수 없음");
				else {
					System.out.println("데이터 업데이트 날짜: " + storeCreated);
				}
				System.out.println("-----------------------------------");
			}
		}
	}

}