package com.taf.model;


import com.taf.util.MyConstants;

import java.util.List;

/**
 * Created by julian on 12/8/15.
 */
public class Post extends BaseModel {
    Long mCreatedAt;
    Long mUpdatedAt;
    List<String> mTags;
    String mDescription;
    String mTitle;
    String mType;
    PostData mData;

    public Long getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Long pCreatedAt) {
        mCreatedAt = pCreatedAt;
    }

    public Long getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Long pUpdatedAt) {
        mUpdatedAt = pUpdatedAt;
    }

    public List<String> getTags() {
        return mTags;
    }

    public void setTags(List<String> pTags) {
        mTags = pTags;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String pDescription) {
        mDescription = pDescription;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String pTitle) {
        mTitle = pTitle;
    }

    public String getType() {
        return mType;
    }

    public void setType(String pType) {
        mType = pType;
    }

    public PostData getData() {
        return mData;
    }

    public void setData(PostData pData) {
        mData = pData;
    }

    @Override
    public int getDataType() {
        if (getType().equalsIgnoreCase("audio")) {
            return MyConstants.Adapter.TYPE_AUDIO;
        } else if (getType().equalsIgnoreCase("video")) {
            return MyConstants.Adapter.TYPE_VIDEO;
        } else if (getType().equalsIgnoreCase("text")) {
            return MyConstants.Adapter.TYPE_TEXT;
        }else{
            return MyConstants.Adapter.TYPE_NEWS;
        }
    }

    public static Post getDummyData(){
        Post post = new Post();
        post.setId(1l);
        post.setType("audio");
        post.setDescription("<p>नेपाल सरकारले वैदेशिक रोजगारलाई व्यवस्थित गर्नका लागि वैदेशिक रोजगारमा जानेहरुलाई के गर्नु हुन्छ, के गर्नु हुदैन, के के तयारी गर्नु पर्छ, त्यँहाको धर्म, संस्कृति, रिति रिवाज, कानुन आदिका वारेमा अभिमुखिकरण तालिमबाट जानकारी पाउन सकिन्छ, यस्तो तालिम २ दिनमा १० घण्टाको हुन्छ, तालिमका लागि जम्मा ७०० रुपैया लाग्छ, त्यो पनि महिलाहरुका लागि निःशुल्क छ ।</p> <p>राजु खड्का <br />आप्रवासन बिग्य, गैरआवासिय नेपाली संघ <br /> </p>");
        post.setTitle("अभिमुखीकरण तालिम कहांबाट लिनुपर्दछ?");

        PostData data = new PostData();
        data.setThumbnail("http://nrna.yipl.com.np/uploads/audio/2ea80bd3668b251117454d21bbc310673b1193d4.jpg");
        data.setMediaUrl("https://storage.googleapis.com/audiopod/22_Where to go for orientation training_RKhadka.mp3");
        post.setData(data);
        return post;
    }
}
