package com.imronreviady.simplestore.viewmodel.blog;

import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.repository.blog.BlogRepository;
import com.imronreviady.simplestore.utils.AbsentLiveData;
import com.imronreviady.simplestore.viewmodel.common.PSViewModel;
import com.imronreviady.simplestore.viewobject.Blog;
import com.imronreviady.simplestore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class BlogViewModel extends PSViewModel {
    private final LiveData<Resource<List<Blog>>> newsFeedData;
    private MutableLiveData<BlogViewModel.TmpDataHolder> newsFeedObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageNewsFeedData;
    private MutableLiveData<BlogViewModel.TmpDataHolder> nextPageNewsFeedObj = new MutableLiveData<>();

    private final LiveData<Resource<Blog>> blogByIdData;
    private MutableLiveData<BlogViewModel.BlogByIdTmpDataHolder> blogByIdObj = new MutableLiveData<>();

    public String shopName, shopId;

    @Inject
    BlogViewModel(BlogRepository repository) {

        newsFeedData = Transformations.switchMap(newsFeedObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNewsFeedList(obj.limit, obj.offset);

        });

        nextPageNewsFeedData = Transformations.switchMap(nextPageNewsFeedObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageNewsFeedList(Config.API_KEY, obj.limit, obj.offset);

        });

        blogByIdData = Transformations.switchMap(blogByIdObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getBlogById(obj.id);

        });

    }

    public void setNewsFeedObj(String limit, String offset) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(limit, offset);

        this.newsFeedObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Blog>>> getNewsFeedData() {
        return newsFeedData;
    }

    public void setNextPageNewsFeedObj(String limit, String offset) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(limit, offset);

        this.nextPageNewsFeedObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPageNewsFeedData() {
        return nextPageNewsFeedData;
    }

    public void setBlogByIdObj(String id) {
        BlogByIdTmpDataHolder blogByIdTmpDataHolder = new BlogByIdTmpDataHolder(id);

        this.blogByIdObj.setValue(blogByIdTmpDataHolder);
    }

    public LiveData<Resource<Blog>> getBlogByIdData() {
        return blogByIdData;
    }


    class TmpDataHolder {

        String  limit, offset;

        public TmpDataHolder(String limit, String offset) {
            this.limit = limit;
            this.offset = offset;
        }
    }

    class BlogByIdTmpDataHolder {

        String id;

        private BlogByIdTmpDataHolder(String id) {
            this.id = id;
        }
    }
}
