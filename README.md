# UltimateCoins

## Permissions
- ultimatecoins.admin - reload command
- ultimatecoins.percentage.\<number\> - percentage of coins dropped on death
- ultimatecoins.mob.multiplier.\<number\> - mob drop multiplier

## config.yml
```yaml
config-version: 1

# Should players drop money on death?
playersLoseMoneyOnDeath:
  enabled: true
  percentage: 10

coins:
  #This is the currency that is used when a coin uses COINENGINE
  coinsengine-currency: "coins"
  # Should MMOCore party integration be enabled?
  # Requires MMOCore & MythicLib installed.
  mmocore-party-integration:
    enabled: true

    # The amount of coins dropped will be multiplied by
    # this number for each player that's in the party.
    multiplier: 1.2

    # INDIVIDUAL - Only the player gets the coins.
    # SHARED - Each player in the party gets the coins.
    # SPLIT - The coins are split evenly between the party.
    pickup: SPLIT

  #notifies player in chat when they pick up a coin
  notify-player-on-pickup: true

  # Should the number have no decimals? WILL BE CHANGED WHEN EACH COIN HAS AN ECONOMY VAULT DEFAULTS TO DECIMAL.
  #OTHER ECONOMY SETTINGS CAN BE SET WHEN CREATING A COIN
  whole: true

  # Should the drops merge together?
  coin-merging: false

  # Should the drops be dropped with a random offset?
  random-offset: false

  # Should players be able to pick up coins dropped by other players?
  can-players-pickup-others: false

  # Should the victim be able to pick up coins they dropped?
  can-player-pickup-own: true

  pickup-sound:
    enabled: true
    sound: ENTITY_EXPERIENCE_ORB_PICKUP
    #values from 0-2
    pitch: 1.2
    #values from 0-1
    volume: 0.5

  hologram-animation:
    enabled: true

    hologram-text: "+%currency_symbol%%amount%"

    #In ticks
    duration: 16
    y-offset: 1.5
    distance-from-player: 1

    hologram-rise:
      enabled: true
      amount: 1

    # Should the number have no decimals? WILL BE CHANGED WHEN EACH COIN HAS AN ECONOMY VAULT DEFAULTS TO DECIMAL.
    #OTHER ECONOMY SETTINGS CAN BE SET WHEN CREATING A COIN
    whole: true

messages:
  no-permission: "<red>No Permission."
  usage: "<red>Usage: /<alias> <reload>"
  reloaded: "<green>Reloaded (<time>ms)"
  player-death-message: "<red>You dropped $<amount>."
  coin-pickup-message: "You picked up %currency_symbol%%amount%."
```
