package com.mergeintervals;

import java.util.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateMerger {
	public static List<DateRange> mergeDateRange(List<DateRange> dateRanges){
		List<DateRange> result = new ArrayList<>();
		Collections.sort(dateRanges, new Comparator<DateRange>() {
	        @Override
	        public int compare(DateRange range1, DateRange range2) {
	            if (range1.getStartDate().equals(range2.getStartDate())) {
	                return range1.getEndDate().compareTo(range2.getEndDate());
	            }
	            return range1.getStartDate().compareTo(range2.getStartDate());
	        }
	    });
		result.add(dateRanges.get(0));
		for(DateRange x : dateRanges) {
			int n = result.size();
			if(x.getStartDate().compareTo(result.get(n-1).getEndDate()) <= 0) {
				DateRange y = result.get(n-1);
				result.remove(n-1);
				DateRange k = new DateRange(y.getStartDate(), x.getEndDate());
				result.add(k);
			}
			else {
				result.add(x);
			}
		}
        return result;
	}
	
	public static void main(String[] args) throws IOException, ParseException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("  ");
		int n = Integer.parseInt(reader.readLine());
		List<DateRange> dateRanges = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		while(n-- > 0) {
			String s = reader.readLine();
			String[] str = s.trim().split(" ");
			Date startDate = sdf.parse(str[0]);
			Date endDate = sdf.parse(str[1]);
			DateRange d = new DateRange(startDate, endDate);
			dateRanges.add(d);
		}
		List<DateRange> res = mergeDateRange(dateRanges);
		for(int i = 0; i < res.size(); ++i) {
			String startDate = sdf.format(res.get(i).getStartDate());
			String endDate = sdf.format(res.get(i).getEndDate());
			System.out.println(startDate+" "+endDate);
		}
	}

}
