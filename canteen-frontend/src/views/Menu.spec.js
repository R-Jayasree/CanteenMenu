import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Menu from './Menu.vue'
import axios from 'axios'

// Mock axios
vi.mock('axios')

describe('Menu.vue', () => {
  it('renders menu items from API', async () => {
    axios.get.mockResolvedValueOnce({
      data: [
        { id: '1', name: 'Pizza', description: 'Yummy', imageUrl: '', price: 99, ratings: [] }
      ]
    })
    const wrapper = mount(Menu)
    await new Promise(resolve => setTimeout(resolve, 0))
    expect(wrapper.html()).toContain('Pizza');
    expect(wrapper.html()).toContain('Yummy');
  })
})
