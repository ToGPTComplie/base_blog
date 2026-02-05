import React, { useEffect, useState } from 'react';
import { Table, Button, Space, message, Tag, Popconfirm } from 'antd';
import { getPostList, deletePost, PostResponse } from '../api/posts';
import { useNavigate } from 'react-router-dom';

const PostList: React.FC = () => {
  const [data, setData] = useState<PostResponse[]>([]);
  const [loading, setLoading] = useState(false);
  const [pagination, setPagination] = useState({ current: 1, pageSize: 10, total: 0 });
  const navigate = useNavigate();

  const fetchData = async (page = 1, size = 10) => {
    setLoading(true);
    try {
      const res = await getPostList(page - 1, size);
      if (res.code === 200) {
        setData(res.data.content);
        setPagination({
          current: res.data.number + 1,
          pageSize: res.data.size,
          total: res.data.totalElements,
        });
      }
    } catch (error) {
      message.error('获取列表失败');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleTableChange = (pagination: any) => {
    fetchData(pagination.current, pagination.pageSize);
  };

  const handleDelete = async (id: number) => {
      try {
          const res = await deletePost(id);
          if (res.code === 200) {
              message.success('删除成功');
              fetchData(pagination.current, pagination.pageSize);
          } else {
              message.error(res.message);
          }
      } catch (e) {
          message.error('删除失败');
      }
  }
  
  const handleLogout = () => {
      localStorage.removeItem('isLoggedIn');
      message.success('退出登录');
      navigate('/login');
  }

  const columns = [
    {
      title: '标题',
      dataIndex: 'title',
      key: 'title',
      render: (text: string, record: PostResponse) => <a onClick={() => navigate(`/post/${record.id}`)}>{text}</a>,
    },
    {
      title: '作者',
      dataIndex: ['author', 'nickname'],
      key: 'author',
    },
    {
      title: '标签',
      dataIndex: 'tags',
      key: 'tags',
      render: (tags: string[]) => (
        <>
          {tags.map((tag) => (
            <Tag color="blue" key={tag}>
              {tag}
            </Tag>
          ))}
        </>
      ),
    },
    {
      title: '阅读量',
      dataIndex: 'pv',
      key: 'pv',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
    },
    {
      title: '操作',
      key: 'action',
      render: (_: any, record: PostResponse) => (
        <Space size="middle">
          <Button onClick={() => navigate(`/post/edit/${record.id}`)}>编辑</Button>
          <Popconfirm title="确定删除吗？" onConfirm={() => handleDelete(record.id)}>
            <Button danger>删除</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
        <h2>文章列表</h2>
        <Space>
           <Button onClick={handleLogout}>退出登录</Button>
           <Button type="primary" onClick={() => navigate('/post/create')}>
             写文章
           </Button>
        </Space>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        rowKey="id"
        pagination={pagination}
        loading={loading}
        onChange={handleTableChange}
      />
    </div>
  );
};

export default PostList;
