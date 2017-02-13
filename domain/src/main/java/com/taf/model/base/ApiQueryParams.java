package com.taf.model.base;

import com.taf.interactor.UseCaseData;

/**
 *
 */
public class ApiQueryParams {

    public String countryId;
    public String categoryId;
    public String gender;
    public String blockId;

    public ApiQueryParams(UseCaseData useCaseData) {
        countryId = useCaseData.getString(UseCaseData.COUNTRY_ID, null);
        categoryId = useCaseData.getString(UseCaseData.CATEGORY_ID, null);
        gender = useCaseData.getString(UseCaseData.USER_GENDER, null);
        blockId = useCaseData.getString(UseCaseData.BLOCK_ID, null);
    }
}
