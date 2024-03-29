package com.quest.service.impl;

import com.quest.entity.Like;
import com.quest.entity.Post;
import com.quest.entity.User;
import com.quest.repository.LikeRepository;
import com.quest.dto.request.LikeCreateRequest;
import com.quest.dto.response.LikeResponse;
import com.quest.service.LikeService;
import com.quest.service.PostService;
import com.quest.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    private LikeRepository likeRepository;
    private PostService postService;
    private UserService userService;

    public LikeServiceImpl(LikeRepository likeRepository, PostService postService, UserService userService) {
        this.likeRepository = likeRepository;
        this.postService = postService;
        this.userService = userService;
    }

    @Override
    public List<LikeResponse> getAllLikesWithParam(Optional<Long> postId, Optional<Long> userId) {
        List<Like> list;
        if (postId.isPresent() && userId.isPresent()) {
            list = likeRepository.findByPostIdAndUserId(postId, userId);
        } else if (postId.isPresent()) {
            list = likeRepository.findByPostId(postId.get());
        } else if (userId.isPresent()) {
            list = likeRepository.findByUserId(userId.get());
        } else
            list = likeRepository.findAll();
        return list.stream().map(like -> new LikeResponse(like)).collect(Collectors.toList());
    }

    @Override
    public Like getOneLikeById(Long likeId) {
        return likeRepository.findById(likeId).orElse(null);
    }

    @Override
    public Like createOneLike(LikeCreateRequest createLike) {
        Post post = postService.getOnePostById(createLike.getPostId());
        User user = userService.getOneUserById(createLike.getUserId());

        if (post != null && user != null) {
            Like likeToSave = new Like();
            likeToSave.setId(createLike.getId());
            likeToSave.setPost(post);
            likeToSave.setUser(user);
            return likeRepository.save(likeToSave);
        } else
            return null;
    }

    @Override
    public void deleteOneLikeById(Long likeId) {
        likeRepository.deleteById(likeId);
    }


}
