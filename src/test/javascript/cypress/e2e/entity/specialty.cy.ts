import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Specialty e2e test', () => {
  const specialtyPageUrl = '/specialty';
  const specialtyPageUrlPattern = new RegExp('/specialty(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const specialtySample = { name: 'architecture Internal iterate' };

  let specialty;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/specialties+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/specialties').as('postEntityRequest');
    cy.intercept('DELETE', '/api/specialties/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (specialty) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/specialties/${specialty.id}`,
      }).then(() => {
        specialty = undefined;
      });
    }
  });

  it('Specialties menu should load Specialties page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('specialty');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Specialty').should('exist');
    cy.url().should('match', specialtyPageUrlPattern);
  });

  describe('Specialty page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(specialtyPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Specialty page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/specialty/new$'));
        cy.getEntityCreateUpdateHeading('Specialty');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialtyPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/specialties',
          body: specialtySample,
        }).then(({ body }) => {
          specialty = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/specialties+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/specialties?page=0&size=20>; rel="last",<http://localhost/api/specialties?page=0&size=20>; rel="first"',
              },
              body: [specialty],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(specialtyPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Specialty page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('specialty');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialtyPageUrlPattern);
      });

      it('edit button click should load edit Specialty page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Specialty');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialtyPageUrlPattern);
      });

      it('edit button click should load edit Specialty page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Specialty');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialtyPageUrlPattern);
      });

      it('last delete button click should delete instance of Specialty', () => {
        cy.intercept('GET', '/api/specialties/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('specialty').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialtyPageUrlPattern);

        specialty = undefined;
      });
    });
  });

  describe('new Specialty page', () => {
    beforeEach(() => {
      cy.visit(`${specialtyPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Specialty');
    });

    it('should create an instance of Specialty', () => {
      cy.get(`[data-cy="name"]`).type('Montserrat olive Drive').should('have.value', 'Montserrat olive Drive');

      cy.get(`[data-cy="description"]`).type('cutting-edge cross-media silver').should('have.value', 'cutting-edge cross-media silver');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        specialty = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', specialtyPageUrlPattern);
    });
  });
});
