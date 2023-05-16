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

describe('Place e2e test', () => {
  const placePageUrl = '/place';
  const placePageUrlPattern = new RegExp('/place(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const placeSample = { name: 'quantify up strategize' };

  let place;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/places+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/places').as('postEntityRequest');
    cy.intercept('DELETE', '/api/places/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (place) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/places/${place.id}`,
      }).then(() => {
        place = undefined;
      });
    }
  });

  it('Places menu should load Places page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('place');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Place').should('exist');
    cy.url().should('match', placePageUrlPattern);
  });

  describe('Place page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(placePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Place page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/place/new$'));
        cy.getEntityCreateUpdateHeading('Place');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', placePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/places',
          body: placeSample,
        }).then(({ body }) => {
          place = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/places+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/places?page=0&size=20>; rel="last",<http://localhost/api/places?page=0&size=20>; rel="first"',
              },
              body: [place],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(placePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Place page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('place');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', placePageUrlPattern);
      });

      it('edit button click should load edit Place page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Place');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', placePageUrlPattern);
      });

      it('edit button click should load edit Place page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Place');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', placePageUrlPattern);
      });

      it('last delete button click should delete instance of Place', () => {
        cy.intercept('GET', '/api/places/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('place').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', placePageUrlPattern);

        place = undefined;
      });
    });
  });

  describe('new Place page', () => {
    beforeEach(() => {
      cy.visit(`${placePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Place');
    });

    it('should create an instance of Place', () => {
      cy.get(`[data-cy="name"]`).type('Loan Cliff').should('have.value', 'Loan Cliff');

      cy.get(`[data-cy="streetAddress"]`).type('Investment invoice').should('have.value', 'Investment invoice');

      cy.get(`[data-cy="postalCode"]`).type('Grocery Hat').should('have.value', 'Grocery Hat');

      cy.get(`[data-cy="city"]`).type('Kuvalisfort').should('have.value', 'Kuvalisfort');

      cy.get(`[data-cy="stateProvince"]`).type('robust Aruban').should('have.value', 'robust Aruban');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        place = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', placePageUrlPattern);
    });
  });
});
