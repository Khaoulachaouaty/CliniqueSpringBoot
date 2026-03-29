export const API_CONFIG = {
  baseUrl: 'http://localhost:8081/clinique/api',
  endpoints: {
    auth: {
      login: '/auth/login',
      registerPatient: '/auth/register/patient',
      createMedecin: '/admin/medecins'
    },
    admin: {
      patients: '/admin/patients',
      medecins: '/admin/medecins'
    },
    public: {
      medecins: '/public/medecins',
      medecinsBySpecialite: '/medecin/specialite'
    }
  }
} as const;