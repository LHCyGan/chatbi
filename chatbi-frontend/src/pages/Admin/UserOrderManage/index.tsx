import type { ProColumns } from '@ant-design/pro-components';
import { ModalForm, ProForm, ProFormText, ProTable } from '@ant-design/pro-components';

import {
  cancelOrderUsingPOST,
  deleteOrderUsingPOST,
  listMyOrderByPageUsingPOST, listOrderByPageUsingPOST,
  updateOrderUsingPOST,
} from '@/services/ShierBI/aiFrequencyOrderController';
import { Link } from '@@/exports';
import { Button, message, Popconfirm, Tag } from 'antd';
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
const columns: ProColumns<API.AiFrequencyOrder>[] = [
  {
    title: '序号',
    dataIndex: 'id',
    valueType: 'indexBorder',
    width: 48,
    align: 'center',
  },
  {
    title: '订单号',
    dataIndex: 'id',
    copyable: true,
    fixed: 'left',
    align: 'center',
  },
  {
    title: '单价',
    dataIndex: 'price',
    ellipsis: true,
    align: 'center',
  },
  {
    title: '购买数量',
    dataIndex: 'purchaseQuantity',
    tip: '买越多💰也越多哦',
    align: 'center',
  },
  {
    title: '总价格',
    dataIndex: 'totalAmount',
    ellipsis: true,
    tip: '买越多付费越多哦',
    align: 'center',
  },
  {
    title: '订单状态',
    dataIndex: 'orderStatus',
    // 枚举
    valueType: 'select',
    valueEnum: {
      0: { text: <Tag color="warning">待支付</Tag>, status: 'Default' },
      1: { text: <Tag color="success">已支付</Tag>, status: 'Success' },
      2: { text: <Tag color="error">超时订单</Tag>, status: 'Error' },
      3: { text: <Tag color="red">订单已取消</Tag>, status: 'Error' },
    },
    align: 'center',
  },
  {
    title: '用户ID',
    dataIndex: 'userId',
    copyable: true,
    align: 'center',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    valueType: 'dateTime',
    align: 'center',
  },
  {
    title: '付款/取消',
    align: 'center',
    valueType: 'option',
    key: 'pay',
    render: (text, record, _, action) => [
      <>
        <Link to="/admin/user_pay_order_manage">
          <Button size={'small'} type={'primary'}>
            付款
          </Button>
        </Link>
        <a key="view">
          <Popconfirm
            title="取消订单"
            description="你确定要取消此订单吗？"
            onConfirm={async (e) => {
              console.log('record', record);
              const isCancel = await cancelOrderUsingPOST({ ...record });
              if (isCancel) {
                message.success('取消成功');
                // 刷新订单信息表单
                location.reload();
              } else {
                message.error('取消失败');
              }
            }}
            onCancel={(e) => {}}
            okText="是"
            cancelText="否"
          >
            <Button size={'small'}>取消</Button>
          </Popconfirm>
        </a>
      </>,
    ],
  },
  {
    title: '操作',
    align: 'center',
    valueType: 'option',
    key: 'option',
    render: (text, record, _, action) => [
      <>
        <ModalForm<API.AiFrequencyOrderQueryRequest>
          title="修改订单信息"
          trigger={
            <Button type="dashed" size={'small'} style={{ color: 'blue' }}>
              修改
            </Button>
          }
          autoFocusFirstInput
          modalProps={{
            destroyOnClose: true,
            onCancel: () => console.log('run'),
          }}
          submitTimeout={2000}
          onFinish={async (values) => {
            //点击了提交，发起请求
            console.log('values', values);
            values.id = record.id;
            const updateOrder = await updateOrderUsingPOST(values);
            if (updateOrder.code === 0) {
              message.success('修改订单成功');
              // 刷新界面
              location.reload();
            } else {
              message.error('修改订单失败');
            }
          }}
        >
          <ProForm.Group>
            <ProFormText
              width="md"
              name="purchaseQuantity"
              label="请输入你想购买AI使用次数"
              placeholder="请输入购买数量"
              initialValue={record.purchaseQuantity}
            />
          </ProForm.Group>
        </ModalForm>
        <a key="view">
          <Popconfirm
            title="删除订单"
            description="你确定要删除此订单吗？"
            onConfirm={async (e) => {
              const id = record.id;
              const isDelete = await deleteOrderUsingPOST({ id: id });
              if (isDelete) {
                message.success('删除成功');
                // 刷新订单信息表单
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
        </a>
      </>,
    ],
  },
];

export default () => {
  const [orderTotal, setOrderTotal] = useState<number>(0);
  const [orderList, setOrderList] = useState<API.AiFrequencyOrderVO[]>();
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
    <>
      <ProTable<API.AiFrequencyOrderQueryRequest>
        headerTitle="下单列表"
        columns={columns}
        // 隐藏查询区域
        // search={false}
        // 获取后端的数据，返回到表格
        // @ts-ignore
        request={async (params = {}, sort, filter) => {
          // console.log(sort, filter);
          await waitTime(100);
          const orderList = await listOrderByPageUsingPOST(params);
          console.log('orderlist', orderList?.data?.records);
          if (orderList.code === 0) {
            setOrderList(orderList?.data?.records ?? []);
            setOrderTotal(orderList?.data?.total ?? 0);
          } else {
            message.error('获取订单列表失败');
          }

          // @ts-ignore
          return { data: orderList.data.records };
        }}
        pagination={{
          // 设置分页
          showTotal: () => `共 ${orderTotal} 条记录`,
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
          total: orderTotal,
          position: ['bottomCenter'],
        }}
      />
    </>
  );
};
