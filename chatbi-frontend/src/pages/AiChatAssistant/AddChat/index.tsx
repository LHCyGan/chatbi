import { aiAssistantUsingPost } from '@/services/chatbi/aiAssiantController';

import {Button, Card, Col, Divider, Form, Input, message, Row, Select, Space, Spin, Upload} from 'antd';
import {ProForm} from "@ant-design/pro-form";
import useForm = ProForm.useForm;
import TextArea from 'antd/es/input/TextArea';
import React, { useState } from 'react';
import ReactMarkdown from 'react-markdown';

/**
 * 添加图表页面
 * @constructor
 */
const AddChat: React.FC = () => {
const [form] = useForm();


const renderers = {
  ol: ({ children }) => <ol>{children}</ol>,
  li: ({ children }) => <li>{children}</li>
};

const markdownData = `# 标题
这是一段 **粗体** 文本，还有一些 *斜体* 文本。

- 列表项 1
- 列表项 2
- 列表项 3
1. 你好
2. 哈哈哈哈
`;



const [submitting, setSubmitting] = useState<boolean>(false);
const [questionResult, setQuestionResult ] = useState<API.BiResponse | null>(null);

  /**
   * 提交
   * @param values
   */
  const onFinish = async (values: any) => {
    console.log(values);

    // 避免重复提交
    if (submitting) {
      return;
    }
    setSubmitting(true);


    // 对接后端，上传数据

    try {
      const res = await aiAssistantUsingPost(values);
        console.log(res);

      if (!res?.data) {
        message.error('分析失败');
      } else {
        message.success('分析成功');
        const result = res.data.questionResult
        console.log(result);

        // const questionResult = JSON.parse(res.data.questionResult ?? '');
        if (!result) {
          throw new Error('解析错误')
        } else {
            console.log("成功");

            setQuestionResult(result);
            console.log(questionResult);
        }
      }
    } catch (e: any) {
      message.error('对话失败，' + e.message);
    }
    setSubmitting(false);
  };

  return (
    <div className="add-chart">
      <Row gutter={24}>
        <Col span={24}>
        <Card>
        <Divider style={{fontWeight: 'bold', color: 'blue'}}>讯飞星火 AI 问答助手</Divider>
        <Form
          form={form}
          name="addChat"
          labelCol={{span: 4}}
          wrapperCol={{span: 18}}
          onFinish={onFinish}
          initialValues={{}}
        >
          <Form.Item
            name="questionName"
            label="问题名称"
            rules={[{required: true, message: '请输入问题名称！'}]}
          >
            <TextArea placeholder="请输入问题名称，比如：如何学习Java？"/>
          </Form.Item>
          <Form.Item
            name="questionGoal"
            label="你的问题"
            rules={[{required: true, message: '请输入问题概述'}]}
          >
            <TextArea placeholder="请输入你的分析需求，比如：我要怎么样更好的去学习Java？"/>
          </Form.Item>

          <Form.Item
            name="questionType"
            label="问题类型"
            rules={[{required: true, message: '请选择输入问题类型！'},
              {
                min:2,
                required:true,
                message:'问题类型不能为空！'
              }]}
          >
            <TextArea placeholder="请输入你的问题类型，比如：Java/Python/GO"/>
          </Form.Item>

          <Form.Item wrapperCol={{span: 16, offset: 4}} style={{textAlign:"center"}}>
            <Space>
              <Button
                type="primary"
                htmlType="submit"
                loading={submitting}
                disabled={submitting}
              >
                上传提问
              </Button>
              <Button htmlType="reset">重置内容</Button>
            </Space>
          </Form.Item>
        </Form>
      </Card>
      <Divider />
        </Col>
        <Col span={24}>
        <Card title={<div style={{ textAlign: 'center' ,color:'darkblue' }}>分析结论</div>}>
            <div style={{ marginBottom: '16px' }}>
                {questionResult ? (
                    <ReactMarkdown renderers={renderers}>{questionResult}</ReactMarkdown>

                  ) : (
                <div>请先在左侧进行提交</div>
              )}
            </div>
          {/* <div style={{ marginBottom: '16px' }}>
            {markdownData ? (
              <ReactMarkdown components={renderers}>{markdownData}</ReactMarkdown>
            ) : (
              <div>请先在左侧进行提交</div>
            )}
          </div> */}
          <Spin spinning={submitting} />
        </Card>
          <Divider />
        </Col>
      </Row>
    </div>
  );
};
export default AddChat;
