import type { ProColumns } from '@ant-design/pro-components';
import { ProTable } from '@ant-design/pro-components';

import { message, Tag } from 'antd';
import { useState } from 'react';
import {listMyPayInfoByPageUsingPOST, listPayInfoByPageUsingPOST} from "@/services/ShierBI/aliPayInfoController";

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
const columns: ProColumns<API.AlipayInfoVO>[] = [
  {
    title: '序号',
    dataIndex: 'id',
    valueType: 'indexBorder',
    width: 48,
    align: 'center',
  },
  {
    title: '支付订单号',
    dataIndex: 'id',
    copyable: true,
    fixed: 'left',
    align: 'center',
  },
  {
    title: '支付流水号',
    dataIndex: 'alipayAccountNo',
    ellipsis: true,
    align: 'center',
    copyable: true,
  },
  {
    title: '支付宝ID',
    dataIndex: 'alipayId',
    align: 'center',
    copyable: true,
  },
  {
    title: '订单号',
    dataIndex: 'orderId',
    ellipsis: true,
    align: 'center',
    copyable: true,
  },
  {
    title: '付款金额',
    dataIndex: 'totalAmount',
    ellipsis: true,
    tip: '买越多付费越多哦',
    align: 'center',
    copyable: true,
  },
  {
    title: '支付状态',
    dataIndex: 'payStatus',
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
];

export default () => {
  const [payInfoTotal, setPayInfoTotal] = useState<number>(0);
  /**
   * 初始值
   */
  const initSearchParams = {
    current: 1,
    pageSize: 15,
    sortField: 'createTime',
    sortOrder: 'desc',
  };

  /**
   * 查询参数
   */
  const [searchParams, setSearchParams] = useState<API.AlipayInfoQueryRequest>({
    ...initSearchParams,
  });
  return (
    <>
      <ProTable<API.AlipayInfoQueryRequest>
        headerTitle="支付订单信息结果查询"
        columns={columns}
        // 隐藏查询区域
        // search={false}
        // 获取后端的数据，返回到表格
        // @ts-ignore
        request={async (params = {}, sort, filter) => {
          // console.log(sort, filter);
          await waitTime(500);
          const payInfoList = await listPayInfoByPageUsingPOST(params);
          console.log('payInfoList', payInfoList?.data?.records);
          if (payInfoList.code === 0) {
            setPayInfoTotal(payInfoList?.data?.total ?? 0);
          } else {
            message.error('获取支付订单列表失败');
          }
          // @ts-ignore
          return { data: payInfoList.data.records };
        }}
        pagination={{
          // 设置分页
          showTotal: () => `共 ${payInfoTotal} 条记录`,
          showSizeChanger: true,
          showQuickJumper: true,
          pageSizeOptions: ['15', '30', '50'],
          onChange: (page, pageSize) => {
            setSearchParams({
              ...searchParams,
              current: page,
              pageSize,
            });
          },
          current: searchParams.current,
          pageSize: searchParams.pageSize,
          total: payInfoTotal,
          position: ['bottomCenter'],
        }}
      />
    </>
  );
};
