import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getPostDetail, PostResponse, publishPost } from '../api/posts';
import { Card, Tag, Button, message, Space } from 'antd';

const PostDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [post, setPost] = useState<PostResponse | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (id) {
      loadData(Number(id));
    }
  }, [id]);

  const loadData = async (postId: number) => {
    try {
      const res = await getPostDetail(postId);
      if (res.code === 200) {
        setPost(res.data);
      } else {
        message.error(res.message);
      }
    } catch (error) {
      message.error('加载失败');
    }
  };
  
  const handlePublish = async () => {
      if(!post) return;
      try {
          const res = await publishPost(post.id);
          if (res.code === 200) {
              message.success('发布成功');
              loadData(post.id);
          }
      } catch(e) {
          message.error('发布失败');
      }
  }

  if (!post) return <div>Loading...</div>;

  return (
    <div style={{ padding: 24, display: 'flex', justifyContent: 'center' }}>
      <Card style={{ width: 800 }} title={post.title} extra={
          <Space>
             <Button onClick={() => navigate('/')}>返回列表</Button>
             {post.status === 'DRAFT' && <Button type="primary" onClick={handlePublish}>发布</Button>}
             <Button onClick={() => navigate(`/post/edit/${post.id}`)}>编辑</Button>
          </Space>
      }>
        <div style={{ marginBottom: 16, color: '#999' }}>
          <span>作者: {post.author.nickname || post.author.username}</span>
          <span style={{ marginLeft: 16 }}>时间: {post.createTime}</span>
          <span style={{ marginLeft: 16 }}>阅读: {post.pv}</span>
          <span style={{ marginLeft: 16 }}>
             状态: <Tag color={post.status === 'PUBLISHED' ? 'green' : 'orange'}>{post.status}</Tag>
          </span>
        </div>
        
        <div style={{ marginBottom: 16 }}>
           {post.tags.map(tag => <Tag key={tag}>{tag}</Tag>)}
        </div>

        <div style={{ whiteSpace: 'pre-wrap' }}>
          {post.content}
        </div>
      </Card>
    </div>
  );
};

export default PostDetail;
