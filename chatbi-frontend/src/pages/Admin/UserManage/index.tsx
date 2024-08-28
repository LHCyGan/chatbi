import { selectAvatarUrl, selectGender, selectUserRole } from '@/constants';
import {
  deleteUserUsingPost,
  listUserByPageUsingPost,
  updateUserUsingPost,
} from '@/services/chatbi/userController';
import type { ProColumns } from '@ant-design/pro-components';
import { ModalForm, ProForm, ProFormText, ProTable } from '@ant-design/pro-components';
import { ProFormSelect } from '@ant-design/pro-form';
import { Button, Image, message, Popconfirm, Tag } from 'antd';
import { useState } from 'react';

export const waitTimePromise = async (time: number = 100) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};

export const waitTime = async (time: number = 100) => {
  await waitTimePromise(time);
};

// 定义列对应后端字段
const columns: ProColumns<API.User>[] = [
  {
    title: '序号',
    dataIndex: 'id',
    valueType: 'indexBorder',
    width: 48,
    align: 'center',
  },
  {
    title: '用户账户',
    dataIndex: 'userAccount',
    copyable: true,
    ellipsis: true,
    tip: '用户名称',
    align: 'center',
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
    render: (_, record) => (
      <div>
        <Image src={record.userAvatar} width="80px" height="80px" />
      </div>
    ),
    copyable: true,
    align: 'center',
  },
  {
    title: '用户名',
    dataIndex: 'userName',
    copyable: true,
    ellipsis: true,
    tip: '用户名称',
    align: 'center',
  },
  {
    title: '编号',
    dataIndex: 'userCode',
    copyable: true,
    align: 'center',
  },
  {
    title: '电话',
    dataIndex: 'phone',
    copyable: true,
    align: 'center',
  },
  {
    title: '邮件',
    dataIndex: 'email',
    copyable: true,
    align: 'center',
  },
  {
    title: '性别',
    dataIndex: 'gender',
    // 枚举
    valueType: 'select',
    valueEnum: {
      男: { text: <Tag color="success">男</Tag> },
      女: { text: <Tag color="error">女</Tag> },
    },
    align: 'center',
  },
  {
    title: '用户状态',
    dataIndex: 'status',
    // 枚举
    valueType: 'select',
    valueEnum: {
      0: { text: <Tag color="success">正常</Tag>, status: 'Success' },
      1: { text: <Tag color="warning">注销</Tag>, status: 'Default' },
      2: { text: <Tag color="error">封号</Tag>, status: 'Error' },
    },
    align: 'center',
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
    // 枚举
    valueType: 'select',
    valueEnum: {
      user: { text: <Tag color="default">普通用户</Tag> },
      admin: { text: <Tag color="success">管理员</Tag> },
      ban: { text: <Tag color="error">封号</Tag>, status: 'Error' },
    },
    align: 'center',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    valueType: 'dateTime',
    align: 'center',
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    valueType: 'dateTime',
    align: 'center',
  },
  {
    title: '操作',
    align: 'center',
    valueType: 'option',
    key: 'option',
    render: (text, record, _, action) => [
      <ModalForm<API.User>
        title="修改用户信息"
        trigger={
            <Button size={'small'} type={"primary"} >
              修改
            </Button>
        }
        autoFocusFirstInput
        modalProps={{
          destroyOnClose: true,
          onCancel: () => console.log('run'),
        }}
        submitTimeout={500}
        onFinish={async (values) => {
          await waitTime(500);
          //点击了提交，发起请求
          values.id = record.id;
          const isModify = await updateUserUsingPost(values);
          if (isModify) {
            message.success('修改成功');
            // 刷新用户信息表单
            location.reload();
            return true;
          }
          return false;
        }}
      >
        <ProForm.Group>
          <ProFormText
            width="md"
            name="userName"
            label="用户名"
            placeholder="请输入用户名"
            initialValue={record.userName}
          />
          <ProFormText
            width="md"
            name="userAccount"
            label="用户账户"
            placeholder="请输入账户"
            initialValue={record.userAccount}
          />
          <ProFormText
            width="md"
            name="userPassword"
            label="用户密码"
            placeholder="请修改密码"
            initialValue={record.userPassword}
          />

          <ProFormSelect
            name="userRole"
            fieldProps={{
              size: 'large',
            }}
            label="用户角色"
            options={selectUserRole}
            initialValue={record.userRole}
            placeholder={'选择用户角色'}
            rules={[
              {
                required: true,
                message: '请选择用户角色',
              },
            ]}
          />
          <ProFormSelect
            name="userAvatar"
            fieldProps={{
              size: 'large',
            }}
            label="用户头像"
            options={selectAvatarUrl}
            placeholder={'请选择用户头像 '}
            initialValue={record.userAvatar}
            rules={[
              {
                required: true,
                message: '请输入选择用户头像!',
              },
            ]}
          />

        </ProForm.Group>
      </ModalForm>,
      <a key="view">
        <Popconfirm
          title="删除用户"
          description="你确定要删除他吗？"
          onConfirm={async (e) => {
            console.log('id', record.id);
            const id = record.id;
            const isDelete = await deleteUserUsingPost({ id: id });
            if (isDelete) {
              message.success('删除成功');
              // 刷新用户信息表单
              location.reload();
            } else {
              message.error('删除失败');
            }
          }}
          onCancel={(e) => {}}
          okText="是"
          cancelText="否"
        >
          <Button size={'small'} type={'primary'} danger>
              删除
            </Button>
        </Popconfirm>
      </a>,
    ],
  },
];

export default () => {
  const [userTotal, setUserTotal] = useState<number>(0);
  /**
   * 初始值
   */
  const initSearchParams = {
    current: 1,
    pageSize: 10,
    sortField: 'createTime',
    sortOrder: 'desc',
  };

  /**
   * 查询参数
   */
  const [searchParams, setSearchParams] = useState<API.AiFrequencyOrderQueryRequest>({
    ...initSearchParams,
  });
  return (
    <ProTable<API.UserQueryRequest>
      columns={columns}
      // 隐藏查询区域
      // search={false}
      // 获取后端的数据，返回到表格
      // @ts-ignore
      request={async (params = {}, sort, filter) => {
        await waitTime(500);
        const userList = await listUserByPageUsingPost(params);
        // console.log(userList.data);
        if (userList.code === 0) {
          setUserTotal(userList.data?.total ?? 0);
        } else {
          message.error('获取用户列表失败');
        }
        // @ts-ignore
        return { data: userList.data.records };
      }}
      pagination={{
        // 设置分页
        showTotal: () => `共 ${userTotal} 条记录`,
        showSizeChanger: true,
        showQuickJumper: true,
        pageSizeOptions: ['10', '20', '30'],
        onChange: (page, pageSize) => {
          setSearchParams({
            ...searchParams,
            current: page,
            pageSize,
          });
        },
        current: searchParams.current,
        pageSize: searchParams.pageSize,
        total: userTotal,
        position: ['bottomCenter'],
      }}
      dateFormatter="string"
      headerTitle="用户列表"
    />
  );
};
