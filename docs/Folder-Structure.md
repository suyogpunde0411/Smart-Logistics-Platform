# Folder Structure

The repository is divided into 4 primary root directories:

## `/backend`
Contains all the Java Spring Boot microservices.
- Driven by a parent `pom.xml` to manage shared dependency versions.
- Standard Spring Boot structure inside each service: `src/main/java`, `src/main/resources`, `src/test`.
- **Packages per service**: `config`, `controller`, `service`, `repository`, `entity`, `dto`, `mapper`, `exception`, `security`, `validation`, `util`, `events`, `client`, `constants`.

## `/frontend`
Contains the React SPA (Single Page Application).
- Written in JavaScript, using Vite as the bundler.
- State management via Redux Toolkit and React Query.
- Styled using TailwindCSS.
- **Key Directories**:
  - `src/features/`: Feature-based modular architecture containing isolated components, pages, hooks, services, and types per business feature (e.g., Dashboard, Matching).
  - `src/common/` & `src/components/`: Reusable, generic UI components.
  - `src/api/` & `src/services/`: API interaction logic.

## `/docs`
Contains all architectural, design, and procedural documentation ensuring consistency across large, distributed development teams.

## `/scripts`
Contains automation scripts (e.g., PowerShell/Bash scripts) used for bootstrapping, deploying, and migrating the application environments.
