import React, { useEffect } from 'react';
import { Form, Input, Button, message, Select, Card } from 'antd';
import { useNavigate, useParams } from 'react-router-dom';
import { createPost, updatePost, getPostDetail, PostCreateRequest } from '../api/posts';

const { Option } = Select;

const PostEdit: React.FC = () => {
  const [form] = Form.useForm();
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEdit = !!id;

  useEffect(() => {
    if (isEdit) {
      loadData(Number(id));
    }
  }, [id]);

  const loadData = async (postId: number) => {
    try {
      const res = await getPostDetail(postId);
      if (res.code === 200) {
        form.setFieldsValue({
            ...res.data,
            tags: res.data.tags
        });
      }
    } catch (error) {
      message.error('加载文章详情失败');
    }
  };

  const onFinish = async (values: PostCreateRequest) => {
    try {
      let res;
      if (isEdit) {
        res = await updatePost(Number(id), values);
      } else {
        res = await createPost(values);
      }

      if (res.code === 200) {
        message.success(isEdit ? '更新成功' : '创建成功');
        navigate('/');
      } else {
        message.error(res.message);
      }
    } catch (error) {
      message.error('操作失败');
    }
  };

  return (
    <div style={{ padding: 24, display: 'flex', justifyContent: 'center' }}>
      <Card title={isEdit ? '编辑文章' : '创建文章'} style={{ width: 800 }}>
        <Form
          form={form}
          layout="vertical"
          onFinish={onFinish}
        >
          <Form.Item
            name="title"
            label="标题"
            rules={[{ required: true, message: '请输入标题' }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            name="summary"
            label="摘要"
          >
            <Input.TextArea rows={2} />
          </Form.Item>

          <Form.Item
            name="content"
            label="内容"
            rules={[{ required: true, message: '请输入内容' }]}
          >
            <Input.TextArea rows={10} />
          </Form.Item>

          <Form.Item
            name="tags"
            label="标签"
          >
            <Select mode="tags" style={{ width: '100%' }} tokenSeparators={[',']}>
            </Select>
          </Form.Item>
          
          <Form.Item
            name="status"
            label="状态"
            initialValue="DRAFT"
          >
             <Select>
                 <Option value="DRAFT">草稿</Option>
                 <Option value="PUBLISHED">发布</Option>
             </Select>
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit">
              提交
            </Button>
            <Button style={{ marginLeft: 8 }} onClick={() => navigate(-1)}>
              取消
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default PostEdit;
