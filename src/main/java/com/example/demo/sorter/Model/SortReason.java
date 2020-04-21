package com.example.demo.sorter.Model;

public enum SortReason {
    //  정상
    SUCCESS,
    //  목적지 없음
    NO_DEST,
    //  바코드 리딩실패
    NO_READ,
    //  슈트 만재
    OVERFLOW,
    //  사용자 막음
    BLOCKED
}
