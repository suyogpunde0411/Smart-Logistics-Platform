import React from 'react';
import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import { ProfileHeader } from '../components/ProfileHeader';

describe('ProfileHeader Component', () => {
  it('renders driver profile header with name and verified status', () => {
    const mockProfile = {
      fullName: 'Ramesh Singh',
      role: 'DRIVER',
      isVerified: true,
      joinedDate: 'Jan 15, 2025',
      avatarUrl: null,
      email: 'ramesh.singh@example.com',
      phone: '+91 98765 43210',
      address: { city: 'Mumbai' }
    };

    render(<ProfileHeader profile={mockProfile} />);

    expect(screen.getByText('Ramesh Singh')).toBeInTheDocument();
    expect(screen.getByText('DRIVER')).toBeInTheDocument();
    expect(screen.getByText('ramesh.singh@example.com')).toBeInTheDocument();
    expect(screen.getByText('+91 98765 43210')).toBeInTheDocument();
    expect(screen.getByText('Joined Jan 15, 2025')).toBeInTheDocument();
  });

  it('renders placeholder name when fullName is missing', () => {
    const mockProfile = {
      role: 'DRIVER',
      isVerified: false,
      joinedDate: 'Jan 15, 2025',
      avatarUrl: null,
      email: 'ramesh.singh@example.com',
      address: {}
    };

    render(<ProfileHeader profile={mockProfile} />);
    expect(screen.getByText('?')).toBeInTheDocument();
  });
});
