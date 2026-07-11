import React from 'react';

export const PasswordStrengthMeter = ({ password }) => {
  const calculateStrength = (pass) => {
    let strength = 0;
    if (pass.length > 7) strength += 1;
    if (/[A-Z]/.test(pass)) strength += 1;
    if (/[a-z]/.test(pass)) strength += 1;
    if (/[0-9]/.test(pass)) strength += 1;
    if (/[^A-Za-z0-9]/.test(pass)) strength += 1;
    return strength;
  };

  const strength = calculateStrength(password || '');

  const getLabel = () => {
    switch (strength) {
      case 0: return '';
      case 1:
      case 2: return 'Weak';
      case 3: return 'Fair';
      case 4: return 'Good';
      case 5: return 'Strong';
      default: return '';
    }
  };

  const getColor = () => {
    if (strength <= 2) return 'bg-red-500';
    if (strength === 3) return 'bg-yellow-500';
    if (strength >= 4) return 'bg-green-500';
    return 'bg-gray-200';
  };

  if (!password) return null;

  return (
    <div className="w-full mt-2">
      <div className="flex justify-between items-center mb-1">
        <span className="text-xs text-muted-foreground">Password strength</span>
        <span className="text-xs font-medium">{getLabel()}</span>
      </div>
      <div className="flex gap-1 h-1.5 w-full">
        {[1, 2, 3, 4, 5].map((level) => (
          <div
            key={level}
            className={`flex-1 rounded-full ${
              level <= strength ? getColor() : 'bg-muted'
            }`}
          />
        ))}
      </div>
    </div>
  );
};
