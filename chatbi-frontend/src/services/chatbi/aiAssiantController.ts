// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** aiAssistant POST /api/aiAssistant/chat */
export async function aiAssistantUsingPost(
  body: API.GenChatByAiRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseObject_>('/api/aiAssistant/chat', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
