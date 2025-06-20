// Initialize color pickers
const colorPickers = {};

// Load and display effects
async function loadEffects() {
    const response = await fetch('/api/effects');
    const data = await response.json();
    const editor = document.getElementById('effectsEditor');
    editor.innerHTML = '';
    
    for (const [key, effect] of Object.entries(data.effects)) {
        const effectDiv = createEffectElement(key, effect);
        editor.appendChild(effectDiv);
    }
    
    // Add new effect button
    const addButton = document.createElement('button');
    addButton.className = 'btn btn-primary mt-3';
    addButton.textContent = 'Add New Effect';
    addButton.onclick = () => {
        const newEffect = {
            display_name: '',
            id: '',
            unluckLevel: 0,
            description: '',
            instant: false,
            color: { red: 255, green: 255, blue: 255 },
            attributes: {}
        };
        const effectDiv = createEffectElement('new_effect', newEffect);
        editor.appendChild(effectDiv);
    };
    editor.appendChild(addButton);
}

// Create effect element
function createEffectElement(key, effect) {
    const div = document.createElement('div');
    div.className = 'card mb-3';
    div.innerHTML = `
        <div class="card-body">
            <h5 class="card-title">
                <input type="text" class="form-control" value="${effect.display_name}" 
                       placeholder="Display Name" onchange="updateEffect('${key}', 'display_name', this.value)">
            </h5>
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label>ID</label>
                        <input type="text" class="form-control" value="${effect.id}" 
                               onchange="updateEffect('${key}', 'id', this.value)">
                    </div>
                    <div class="mb-3">
                        <label>Description</label>
                        <textarea class="form-control" 
                                  onchange="updateEffect('${key}', 'description', this.value)">${effect.description || ''}</textarea>
                    </div>
                    <div class="mb-3">
                        <label>Unluck Level</label>
                        <input type="number" class="form-control" value="${effect.unluckLevel}" 
                               onchange="updateEffect('${key}', 'unluckLevel', parseInt(this.value))">
                    </div>
                    <div class="mb-3">
                        <label>Color</label>
                        <div class="color-picker" id="color-${key}"></div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <label>Particle</label>
                        <select class="form-control" onchange="updateEffect('${key}', 'particle', this.value)">
                            <option value="">Select particle...</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label>Attributes</label>
                        <div class="attributes-container" id="attributes-${key}"></div>
                        <button class="btn btn-sm btn-secondary mt-2" 
                                onclick="addAttribute('${key}')">Add Attribute</button>
                    </div>
                </div>
            </div>
            <button class="btn btn-danger" onclick="deleteEffect('${key}')">Delete Effect</button>
        </div>
    `;
    
    // Initialize color picker
    const pickr = Pickr.create({
        el: `#color-${key}`,
        theme: 'classic',
        default: `rgb(${effect.color?.red || 255}, ${effect.color?.green || 255}, ${effect.color?.blue || 255})`,
        components: {
            preview: true,
            opacity: true,
            hue: true,
            interaction: {
                hex: true,
                rgba: true,
                input: true,
                clear: true,
                save: true
            }
        }
    });
    
    pickr.on('save', (color) => {
        const rgb = color.toRGB();
        updateEffect(key, 'color', { red: rgb.r, green: rgb.g, blue: rgb.b });
    });
    
    colorPickers[key] = pickr;
    
    // Load particle options
    loadParticleOptions(key, effect.particle);
    
    // Load attributes
    loadAttributes(key, effect.attributes || {});
    
    return div;
}

// Load particle options
async function loadParticleOptions(key, selectedParticle) {
    const response = await fetch('/api/suggestions/effects');
    const data = await response.json();
    const select = document.querySelector(`#color-${key}`).parentElement.nextElementSibling.querySelector('select');
    
    data.particles.forEach(particle => {
        const option = document.createElement('option');
        option.value = particle;
        option.textContent = particle;
        if (particle === selectedParticle) {
            option.selected = true;
        }
        select.appendChild(option);
    });
}

// Load attributes
async function loadAttributes(key, attributes) {
    const container = document.getElementById(`attributes-${key}`);
    container.innerHTML = '';
    
    for (const [attrKey, attr] of Object.entries(attributes)) {
        const attrDiv = createAttributeElement(key, attrKey, attr);
        container.appendChild(attrDiv);
    }
}

// Create attribute element
function createAttributeElement(effectKey, attrKey, attr) {
    const div = document.createElement('div');
    div.className = 'mb-2';
    div.innerHTML = `
        <div class="input-group">
            <select class="form-control" onchange="updateAttribute('${effectKey}', '${attrKey}', 'name', this.value)">
                <option value="">Select attribute...</option>
            </select>
            <select class="form-control" onchange="updateAttribute('${effectKey}', '${attrKey}', 'operation', this.value)">
                <option value="">Select operation...</option>
            </select>
            <input type="number" class="form-control" value="${attr.amount}" 
                   onchange="updateAttribute('${effectKey}', '${attrKey}', 'amount', parseFloat(this.value))">
            <button class="btn btn-danger" onclick="removeAttribute('${effectKey}', '${attrKey}')">×</button>
        </div>
    `;
    
    // Load attribute options
    loadAttributeOptions(effectKey, attrKey, attr);
    
    return div;
}

// Load attribute options
async function loadAttributeOptions(effectKey, attrKey, attr) {
    const response = await fetch('/api/suggestions/effects');
    const data = await response.json();
    const select = document.querySelector(`#attributes-${effectKey}`).lastElementChild.querySelector('select');
    
    data.attributes.forEach(attribute => {
        const option = document.createElement('option');
        option.value = attribute;
        option.textContent = attribute;
        if (attribute === attrKey) {
            option.selected = true;
        }
        select.appendChild(option);
    });
    
    // Load operation options
    const operationSelect = select.nextElementSibling;
    data.operations.forEach(operation => {
        const option = document.createElement('option');
        option.value = operation;
        option.textContent = operation;
        if (operation === attr.operation) {
            option.selected = true;
        }
        operationSelect.appendChild(option);
    });
}

// Update effect
async function updateEffect(key, field, value) {
    const response = await fetch('/api/effects');
    const data = await response.json();
    
    if (!data.effects[key]) {
        data.effects[key] = {
            display_name: '',
            id: '',
            unluckLevel: 0,
            attributes: {}
        };
    }
    
    data.effects[key][field] = value;
    
    await fetch('/api/effects', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
}

// Update attribute
async function updateAttribute(effectKey, attrKey, field, value) {
    const response = await fetch('/api/effects');
    const data = await response.json();
    
    if (!data.effects[effectKey].attributes) {
        data.effects[effectKey].attributes = {};
    }
    
    if (field === 'name') {
        const oldAttr = data.effects[effectKey].attributes[attrKey];
        delete data.effects[effectKey].attributes[attrKey];
        data.effects[effectKey].attributes[value] = oldAttr;
    } else {
        if (!data.effects[effectKey].attributes[attrKey]) {
            data.effects[effectKey].attributes[attrKey] = {};
        }
        data.effects[effectKey].attributes[attrKey][field] = value;
    }
    
    await fetch('/api/effects', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
}

// Add attribute
function addAttribute(effectKey) {
    const container = document.getElementById(`attributes-${effectKey}`);
    const attrKey = `new_attr_${Date.now()}`;
    const attr = { operation: 'ADD_NUMBER', amount: 0 };
    const attrDiv = createAttributeElement(effectKey, attrKey, attr);
    container.appendChild(attrDiv);
}

// Remove attribute
async function removeAttribute(effectKey, attrKey) {
    const response = await fetch('/api/effects');
    const data = await response.json();
    
    delete data.effects[effectKey].attributes[attrKey];
    
    await fetch('/api/effects', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    
    loadAttributes(effectKey, data.effects[effectKey].attributes);
}

// Delete effect
async function deleteEffect(key) {
    const response = await fetch('/api/effects');
    const data = await response.json();
    
    delete data.effects[key];
    
    await fetch('/api/effects', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    
    loadEffects();
}

// Load and display potions
async function loadPotions() {
    const response = await fetch('/api/potions');
    const data = await response.json();
    const editor = document.getElementById('potionsEditor');
    editor.innerHTML = '';
    
    for (const [key, potion] of Object.entries(data.potions)) {
        const potionDiv = createPotionElement(key, potion);
        editor.appendChild(potionDiv);
    }
    
    // Add new potion button
    const addButton = document.createElement('button');
    addButton.className = 'btn btn-primary mt-3';
    addButton.textContent = 'Add New Potion';
    addButton.onclick = () => {
        const newPotion = {
            id: '',
            display_name: '',
            RequredInPot: 'WATER',
            BrewingTime: 15,
            BrewingStatus: 'neutral',
            lit: '123',
            ingredients: [],
            color: 'FFFFFF',
            effects: []
        };
        const potionDiv = createPotionElement('new_potion', newPotion);
        editor.appendChild(potionDiv);
    };
    editor.appendChild(addButton);
}

// Create potion element
function createPotionElement(key, potion) {
    const div = document.createElement('div');
    div.className = 'card mb-3';
    div.innerHTML = `
        <div class="card-body">
            <h5 class="card-title">
                <input type="text" class="form-control" value="${potion.display_name}" 
                       placeholder="Display Name" onchange="updatePotion('${key}', 'display_name', this.value)">
            </h5>
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label>ID</label>
                        <input type="text" class="form-control" value="${potion.id}" 
                               onchange="updatePotion('${key}', 'id', this.value)">
                    </div>
                    <div class="mb-3">
                        <label>Required in Pot</label>
                        <select class="form-control" onchange="updatePotion('${key}', 'RequredInPot', this.value)">
                            <option value="WATER" ${potion.RequredInPot === 'WATER' ? 'selected' : ''}>Water</option>
                            <option value="base_potion" ${potion.RequredInPot === 'base_potion' ? 'selected' : ''}>Base Potion</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label>Brewing Time (seconds)</label>
                        <input type="number" class="form-control" value="${potion.BrewingTime}" 
                               onchange="updatePotion('${key}', 'BrewingTime', parseInt(this.value))">
                    </div>
                    <div class="mb-3">
                        <label>Brewing Status</label>
                        <select class="form-control" onchange="updatePotion('${key}', 'BrewingStatus', this.value)">
                            <option value="good" ${potion.BrewingStatus === 'good' ? 'selected' : ''}>Good</option>
                            <option value="neutral" ${potion.BrewingStatus === 'neutral' ? 'selected' : ''}>Neutral</option>
                            <option value="worse" ${potion.BrewingStatus === 'worse' ? 'selected' : ''}>Worse</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label>Fire Level (1-3)</label>
                        <input type="text" class="form-control" value="${potion.lit}" 
                               onchange="updatePotion('${key}', 'lit', this.value)">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <label>Color (HEX)</label>
                        <div class="color-picker" id="potion-color-${key}"></div>
                    </div>
                    <div class="mb-3">
                        <label>Ingredients</label>
                        <div class="ingredients-container" id="ingredients-${key}"></div>
                        <button class="btn btn-sm btn-secondary mt-2" 
                                onclick="addIngredient('${key}')">Add Ingredient</button>
                    </div>
                    <div class="mb-3">
                        <label>Effects</label>
                        <div class="effects-container" id="potion-effects-${key}"></div>
                        <button class="btn btn-sm btn-secondary mt-2" 
                                onclick="addPotionEffect('${key}')">Add Effect</button>
                    </div>
                </div>
            </div>
            <button class="btn btn-danger" onclick="deletePotion('${key}')">Delete Potion</button>
        </div>
    `;
    
    // Initialize color picker
    const pickr = Pickr.create({
        el: `#potion-color-${key}`,
        theme: 'classic',
        default: `#${potion.color}`,
        components: {
            preview: true,
            opacity: true,
            hue: true,
            interaction: {
                hex: true,
                rgba: true,
                input: true,
                clear: true,
                save: true
            }
        }
    });
    
    pickr.on('save', (color) => {
        updatePotion(key, 'color', color.toHEX().slice(1));
    });
    
    colorPickers[`potion-${key}`] = pickr;
    
    // Load ingredients
    loadIngredients(key, potion.ingredients);
    
    // Load effects
    loadPotionEffects(key, potion.effects);
    
    return div;
}

// Load ingredients
async function loadIngredients(key, ingredients) {
    const container = document.getElementById(`ingredients-${key}`);
    container.innerHTML = '';
    
    for (const ingredient of ingredients) {
        const ingredientDiv = createIngredientElement(key, ingredient);
        container.appendChild(ingredientDiv);
    }
}

// Create ingredient element
function createIngredientElement(potionKey, ingredient) {
    const div = document.createElement('div');
    div.className = 'mb-2';
    div.innerHTML = `
        <div class="input-group">
            <select class="form-control" onchange="updateIngredient('${potionKey}', '${ingredient.material}', 'material', this.value)">
                <option value="">Select ingredient...</option>
            </select>
            <input type="number" class="form-control" value="${ingredient.amount}" 
                   onchange="updateIngredient('${potionKey}', '${ingredient.material}', 'amount', parseInt(this.value))">
            <button class="btn btn-danger" onclick="removeIngredient('${potionKey}', '${ingredient.material}')">×</button>
        </div>
    `;
    
    // Load ingredient options
    loadIngredientOptions(potionKey, ingredient);
    
    return div;
}

// Load ingredient options
async function loadIngredientOptions(potionKey, ingredient) {
    const response = await fetch('/api/suggestions/potions');
    const data = await response.json();
    const select = document.querySelector(`#ingredients-${potionKey}`).lastElementChild.querySelector('select');
    
    data.common_ingredients.forEach(item => {
        const option = document.createElement('option');
        option.value = item;
        option.textContent = item;
        if (item === ingredient.material) {
            option.selected = true;
        }
        select.appendChild(option);
    });
}

// Load potion effects
async function loadPotionEffects(key, effects) {
    const container = document.getElementById(`potion-effects-${key}`);
    container.innerHTML = '';
    
    for (const effect of effects) {
        const effectDiv = createPotionEffectElement(key, effect);
        container.appendChild(effectDiv);
    }
}

// Create potion effect element
function createPotionEffectElement(potionKey, effect) {
    const div = document.createElement('div');
    div.className = 'mb-2';
    div.innerHTML = `
        <div class="input-group">
            <select class="form-control" onchange="updatePotionEffect('${potionKey}', '${effect.type}', 'type', this.value)">
                <option value="">Select effect...</option>
            </select>
            <input type="number" class="form-control" value="${effect.duration}" 
                   onchange="updatePotionEffect('${potionKey}', '${effect.type}', 'duration', parseInt(this.value))"
                   placeholder="Duration">
            <input type="number" class="form-control" value="${effect.level}" 
                   onchange="updatePotionEffect('${potionKey}', '${effect.type}', 'level', parseInt(this.value))"
                   placeholder="Level">
            <button class="btn btn-danger" onclick="removePotionEffect('${potionKey}', '${effect.type}')">×</button>
        </div>
    `;
    
    // Load effect options
    loadPotionEffectOptions(potionKey, effect);
    
    return div;
}

// Load potion effect options
async function loadPotionEffectOptions(potionKey, effect) {
    const response = await fetch('/api/suggestions/potions');
    const data = await response.json();
    const select = document.querySelector(`#potion-effects-${potionKey}`).lastElementChild.querySelector('select');
    
    data.common_effects.forEach(effectType => {
        const option = document.createElement('option');
        option.value = effectType;
        option.textContent = effectType;
        if (effectType === effect.type) {
            option.selected = true;
        }
        select.appendChild(option);
    });
}

// Update potion
async function updatePotion(key, field, value) {
    const response = await fetch('/api/potions');
    const data = await response.json();
    
    if (!data.potions[key]) {
        data.potions[key] = {
            id: '',
            display_name: '',
            RequredInPot: 'WATER',
            BrewingTime: 15,
            BrewingStatus: 'neutral',
            lit: '123',
            ingredients: [],
            color: 'FFFFFF',
            effects: []
        };
    }
    
    data.potions[key][field] = value;
    
    await fetch('/api/potions', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
}

// Update ingredient
async function updateIngredient(potionKey, oldMaterial, field, value) {
    const response = await fetch('/api/potions');
    const data = await response.json();
    
    const ingredientIndex = data.potions[potionKey].ingredients.findIndex(i => i.material === oldMaterial);
    
    if (field === 'material') {
        data.potions[potionKey].ingredients[ingredientIndex].material = value;
    } else {
        data.potions[potionKey].ingredients[ingredientIndex].amount = value;
    }
    
    await fetch('/api/potions', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
}

// Add ingredient
function addIngredient(potionKey) {
    const container = document.getElementById(`ingredients-${potionKey}`);
    const ingredient = { material: '', amount: 1 };
    const ingredientDiv = createIngredientElement(potionKey, ingredient);
    container.appendChild(ingredientDiv);
}

// Remove ingredient
async function removeIngredient(potionKey, material) {
    const response = await fetch('/api/potions');
    const data = await response.json();
    
    data.potions[potionKey].ingredients = data.potions[potionKey].ingredients.filter(i => i.material !== material);
    
    await fetch('/api/potions', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    
    loadIngredients(potionKey, data.potions[potionKey].ingredients);
}

// Update potion effect
async function updatePotionEffect(potionKey, oldType, field, value) {
    const response = await fetch('/api/potions');
    const data = await response.json();
    
    const effectIndex = data.potions[potionKey].effects.findIndex(e => e.type === oldType);
    
    if (field === 'type') {
        data.potions[potionKey].effects[effectIndex].type = value;
    } else if (field === 'duration') {
        data.potions[potionKey].effects[effectIndex].duration = value;
    } else {
        data.potions[potionKey].effects[effectIndex].level = value;
    }
    
    await fetch('/api/potions', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
}

// Add potion effect
function addPotionEffect(potionKey) {
    const container = document.getElementById(`potion-effects-${potionKey}`);
    const effect = { type: '', duration: 0, level: 0 };
    const effectDiv = createPotionEffectElement(potionKey, effect);
    container.appendChild(effectDiv);
}

// Remove potion effect
async function removePotionEffect(potionKey, type) {
    const response = await fetch('/api/potions');
    const data = await response.json();
    
    data.potions[potionKey].effects = data.potions[potionKey].effects.filter(e => e.type !== type);
    
    await fetch('/api/potions', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    
    loadPotionEffects(potionKey, data.potions[potionKey].effects);
}

// Delete potion
async function deletePotion(key) {
    const response = await fetch('/api/potions');
    const data = await response.json();
    
    delete data.potions[key];
    
    await fetch('/api/potions', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    
    loadPotions();
}

// Load effects and potions on page load
document.addEventListener('DOMContentLoaded', () => {
    loadEffects();
    loadPotions();
}); 